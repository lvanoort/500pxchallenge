package com.lukevanoort.chall500px.gallery

import android.util.Log
import com.lukevanoort.chall500px.detail.PhotoDetailRepository
import com.lukevanoort.chall500px.navigation.NavigationLocation
import com.lukevanoort.chall500px.navigation.NavigationRepository
import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


sealed class GalleryViewState {
    data class IdleGallery(
        override val pictures: List<Photo>
    ) : GalleryViewState()
    data class LoadingGallery(
        override val pictures: List<Photo>
    ) : GalleryViewState()

    abstract val pictures: List<Photo>

}

interface GalleryViewModel {
    fun getState() : Observable<GalleryViewState>
    fun loadMore()
    fun reload()
    fun entrySelected(entry: Photo)
}


class GalleryViewModelImpl @Inject constructor(
    private val galleryRepo: GalleryRepository,
    private val detailRepo: PhotoDetailRepository,
    private val navigationRepo: NavigationRepository
): GalleryViewModel {
    override fun getState(): Observable<GalleryViewState> {
        return galleryRepo.getState().map {
            when(it) {
                is GalleryRepositoryState.Loaded -> GalleryViewState.IdleGallery(it.contents)
                is GalleryRepositoryState.Loading -> GalleryViewState.LoadingGallery(it.previousContents)
                is GalleryRepositoryState.Reloading -> GalleryViewState.LoadingGallery(it.previousContents)
            }
        }
    }

    override fun loadMore() {
        galleryRepo.loadNextPage()
    }

    override fun reload() {
        galleryRepo.reload()
    }

    override fun entrySelected(entry: Photo) {
        detailRepo.cachePhoto(entry)
        navigationRepo.navigateTo(NavigationLocation.Detail(entry.id))
    }
}