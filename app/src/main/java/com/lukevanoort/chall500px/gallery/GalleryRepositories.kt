package com.lukevanoort.chall500px.gallery

import com.lukevanoort.chall500px.*
import com.lukevanoort.chall500px.gallery.retrofit.PhotoResponse
import com.lukevanoort.chall500px.gallery.retrofit.PopularPhotoService
import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.random.Random


sealed class GalleryRepositoryState {
    data class Loaded(
        val contents: List<Photo>
    ) : GalleryRepositoryState()

    data class Loading(
        val previousContents: List<Photo>
    ) : GalleryRepositoryState()

    data class Reloading(
        val previousContents: List<Photo>
    ) : GalleryRepositoryState()
}

private data class RepoPhoto(
    override val id: Int,
    override val thumbUrl: String = ""
): Photo {
    override val width: Int
    override val height: Int
    init {
        val rnd = Random(id)
        width = when(rnd.nextInt(3)) {
            0 -> 1024
            1 -> 500
            2 -> 650
            else -> 800
        }
        height = when(rnd.nextInt(3)) {
            0 -> 1024
            1 -> 500
            2 -> 650
            else -> 800
        }
    }

}

interface GalleryRepository {

    fun getState() : Observable<GalleryRepositoryState>
    fun loadNextPage()
    fun reload()
}

@AppScope
class MockGalleryRepository @Inject  constructor(
    @IOScheduler private val scheduler: Scheduler
) : GalleryRepository {
    private sealed class InternalState {
        object LoadingNextPage : InternalState()
        object ReloadingData : InternalState()
        object Idle : InternalState()
    }

    private var currentState = AtomicReference<InternalState>(InternalState.Idle)

    private var contents: List<RepoPhoto> = emptyList()
    private var startingPage: Int = 0
    private var endingPage: Int = 0

    private val subject: BehaviorSubject<GalleryRepositoryState> = BehaviorSubject.createDefault(
        GalleryRepositoryState.Loaded(
            contents = listOf()
        ))

    override fun getState(): Observable<GalleryRepositoryState> {
        return subject
    }

    override fun loadNextPage() {
        if (currentState.compareAndSet(InternalState.Idle,InternalState.LoadingNextPage)) {
            subject.onNext(GalleryRepositoryState.Reloading(contents))
            Observable.fromCallable {
                Thread.sleep(500)
                Any()
            }.subscribeOn(scheduler)
                .subscribe {
                    val mutableContents = contents.toMutableList()
                    for (i in 0..20) {
                        mutableContents.add(
                            RepoPhoto(
                                id = endingPage*100+i
                            )
                        )
                    }
                    endingPage++
                    contents = mutableContents.toList()
                    subject.onNext(GalleryRepositoryState.Loaded(contents))

                    currentState.set(InternalState.Idle)
                }
        }
    }

    override fun reload() {
        if (currentState.compareAndSet(InternalState.Idle,InternalState.ReloadingData)) {
            subject.onNext(GalleryRepositoryState.Loading(contents))
            Observable.fromCallable {
                Thread.sleep(500)
                Any()
            }.subscribeOn(scheduler)
                .subscribe {
                    endingPage = 0
                    startingPage = 1
                    val mutableContents = mutableListOf<RepoPhoto>()
                    for (i in 0..20) {
                        mutableContents.add(
                            RepoPhoto(
                                id = endingPage*1000+i
                            )
                        )
                    }
                    contents = mutableContents.toList()
                    subject.onNext(GalleryRepositoryState.Loaded(contents))
                    currentState.set(InternalState.Idle)
                }
        }
    }

}

@AppScope
class LiveGalleryRepository @Inject  constructor(
    private val service: PopularPhotoService,
    @ApiConsumerKey private val consumerKey: String
) : GalleryRepository {
    private sealed class InternalState {
        object LoadingNextPage : InternalState()
        object ReloadingData : InternalState()
        object Idle : InternalState()
    }

    private var currentState = AtomicReference<InternalState>(InternalState.Idle)

    private var contents: List<PhotoResponse> = emptyList()
    private var endingPage: Int = 0

    private val subject: BehaviorSubject<GalleryRepositoryState> = BehaviorSubject.createDefault(
        GalleryRepositoryState.Loaded(
            contents = listOf()
        ))

    override fun getState(): Observable<GalleryRepositoryState> {
        return subject
    }

    override fun loadNextPage() {
        if (currentState.compareAndSet(InternalState.Idle,InternalState.LoadingNextPage)) {
            subject.onNext(GalleryRepositoryState.Reloading(contents))

            val nextPage = endingPage+1
            subject.onNext(GalleryRepositoryState.Loading(contents))
            service.getPopularPhotos(consumerKey,nextPage)
                .subscribe {
                    val mutable = contents.toMutableList()
                    mutable.addAll(it.photos)
                    contents = mutable.toList()
                    endingPage = nextPage
                    subject.onNext(GalleryRepositoryState.Loaded(contents))
                    currentState.set(InternalState.Idle)
                }
        }
    }

    override fun reload() {
        if (currentState.compareAndSet(InternalState.Idle,InternalState.ReloadingData)) {
            subject.onNext(GalleryRepositoryState.Loading(contents))
            service.getPopularPhotos(consumerKey,1)
                .subscribe {
                    contents = it.photos
                    endingPage = 1
                    subject.onNext(GalleryRepositoryState.Loaded(contents))
                    currentState.set(InternalState.Idle)
                }
        }
    }

}