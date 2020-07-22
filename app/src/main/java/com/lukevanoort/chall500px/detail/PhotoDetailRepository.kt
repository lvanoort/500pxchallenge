package com.lukevanoort.chall500px.detail

import android.util.Log
import com.lukevanoort.chall500px.*
import com.lukevanoort.chall500px.photo.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.random.Random


sealed class PhotoDetailRepositoryState {
    data class Loaded(
        val photo: Photo
    ) : PhotoDetailRepositoryState()

    object Loading : PhotoDetailRepositoryState()
}

interface PhotoDetailRepository {
    fun getState(photoID: Int) : Observable<PhotoDetailRepositoryState>
    fun cachePhoto(photo: Photo)
}

@AppScope
class MockPhotoDetailRepository @Inject  constructor(
    private val service: IndividualPhotoService,
    @ApiConsumerKey private val consumerKey: String
) : PhotoDetailRepository {
    private val cache: MutableMap<Int,Photo> = ConcurrentHashMap()

    override fun getState(photoID: Int): Observable<PhotoDetailRepositoryState> {
        val value = cache[photoID]
        return if (value == null) {
            Observable.just(PhotoDetailRepositoryState.Loaded(MockPhoto(id = photoID)))
        } else {
            Observable.just(PhotoDetailRepositoryState.Loaded(value))
        }
    }

    override fun cachePhoto(photo: Photo) {
        cache[photo.id] = photo
    }

}

@AppScope
class LivePhotoDetailRepository @Inject  constructor(
    private val service: IndividualPhotoService,
    @ApiConsumerKey private val consumerKey: String
) : PhotoDetailRepository {
    private val cache: MutableMap<Int,Photo> = ConcurrentHashMap()

    override fun getState(photoID: Int): Observable<PhotoDetailRepositoryState> {
        val value = cache[photoID]
        return if (value == null) {
            Observable.merge(
                Observable.just(PhotoDetailRepositoryState.Loading),
                service.getPhoto(photoID,consumerKey).map {
                    PhotoDetailRepositoryState.Loaded(it.photo)
                }
            ).retryWhen {
                it.delay(500,TimeUnit.MILLISECONDS)
            }
        } else {
            Observable.just(PhotoDetailRepositoryState.Loaded(value))
        }
    }

    override fun cachePhoto(photo: Photo) {
        cache[photo.id] = photo
    }

}