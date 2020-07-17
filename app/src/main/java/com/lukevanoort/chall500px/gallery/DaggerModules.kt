package com.lukevanoort.chall500px.gallery

import com.lukevanoort.chall500px.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

sealed class GalleryImageLoaderConfig {
    object Mock : GalleryImageLoaderConfig()
    object Live: GalleryImageLoaderConfig()
}

sealed class GalleryRepositoryConfig {
    object Mock : GalleryRepositoryConfig()
    object Live: GalleryRepositoryConfig()
}

@Module
class GalleryAppModules {
    @Provides
    fun getGalleryViewModel(impl: GalleryViewModelImpl) : GalleryViewModel = impl

    @Provides
    @AppScope
    fun getGalleryPhotoLoader(
        galleryImageLoaderConfig: GalleryImageLoaderConfig,
        mockProvider: Provider<MockGalleryPhotoLoader>
    ) : GalleryPhotoLoader = when(galleryImageLoaderConfig) {
        GalleryImageLoaderConfig.Mock -> mockProvider.get()
        GalleryImageLoaderConfig.Live -> TODO("live loader doesn't exist")
    }

    @Provides
    @AppScope
    fun getGalleryRepository(
        galleryRepositoryConfig: GalleryRepositoryConfig,
        mockProvider: Provider<MockGalleryRepository>
    ) : GalleryRepository = when(galleryRepositoryConfig) {
        GalleryRepositoryConfig.Mock -> mockProvider.get()
        GalleryRepositoryConfig.Live -> TODO("live loader doesn't exist")
    }
}