package com.lukevanoort.chall500px.gallery

import com.lukevanoort.chall500px.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Provider


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
    fun getGalleryRepository(
        galleryRepositoryConfig: GalleryRepositoryConfig,
        mockProvider: Provider<MockGalleryRepository>,
        liveProvider: Provider<LiveGalleryRepository>
    ) : GalleryRepository = when(galleryRepositoryConfig) {
        GalleryRepositoryConfig.Mock -> mockProvider.get()
        GalleryRepositoryConfig.Live -> liveProvider.get()
    }
}