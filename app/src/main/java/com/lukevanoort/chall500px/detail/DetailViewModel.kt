package com.lukevanoort.chall500px.detail

import android.util.Log
import com.lukevanoort.chall500px.navigation.NavigationRepository
import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed class DetailViewState {
    data class LoadedData(
        val photo: Photo
    ) : DetailViewState()
    object Loading : DetailViewState()
}

interface DetailViewModel {
    fun getState() : Observable<DetailViewState>
    fun exit()
}

class DetailViewModelFactory @Inject constructor(
    private val navigationRepo: NavigationRepository,
    private val photoDetailRepository: PhotoDetailRepository
) {
    fun provideDetailViewMode(photoId: Int) : DetailViewModel {
        return DetailViewModelImpl(navigationRepo,photoDetailRepository,photoId)
    }
}

class DetailViewModelImpl @Inject constructor(
    private val navigationRepo: NavigationRepository,
    private val photoDetailRepository: PhotoDetailRepository,
    private val photoId: Int
): DetailViewModel {
    override fun getState(): Observable<DetailViewState> {
        return photoDetailRepository.getState(photoId).map {
            when(it) {
                is PhotoDetailRepositoryState.Loaded -> {
                    DetailViewState.LoadedData(it.photo)
                }
                PhotoDetailRepositoryState.Loading -> {
                    DetailViewState.Loading
                }
            }
        }
    }

    override fun exit() {
        navigationRepo.goBack()
    }
}