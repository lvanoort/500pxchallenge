package com.lukevanoort.chall500px.gallery

import android.util.Log
import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
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
    private val repo: GalleryRepository
): GalleryViewModel {
    override fun getState(): Observable<GalleryViewState> {
        return repo.getState().map {
            when(it) {
                is GalleryRepositoryState.Loaded -> GalleryViewState.IdleGallery(it.contents)
                is GalleryRepositoryState.Loading -> GalleryViewState.LoadingGallery(it.previousContents)
                is GalleryRepositoryState.Reloading -> GalleryViewState.LoadingGallery(it.previousContents)
            }
        }
    }

    override fun loadMore() {
        repo.loadNextPage()
    }

    override fun reload() {
        repo.reload()
    }

    override fun entrySelected(entry: Photo) {
    }

}