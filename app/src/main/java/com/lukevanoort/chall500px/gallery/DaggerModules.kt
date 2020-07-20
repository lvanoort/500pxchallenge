package com.lukevanoort.chall500px.gallery

import com.lukevanoort.chall500px.AppScope
import com.lukevanoort.chall500px.IOScheduler
import com.lukevanoort.chall500px.gallery.retrofit.PopularPhotoService
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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
        mockProvider: Provider<MockGalleryPhotoLoader>,
        coilProvider: Provider<CoilPhotoLoader>
    ) : GalleryPhotoLoader = when(galleryImageLoaderConfig) {
        GalleryImageLoaderConfig.Mock -> mockProvider.get()
        GalleryImageLoaderConfig.Live -> coilProvider.get()
    }

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

    @Provides
    @AppScope
    fun providePopularPhoto(retrofit: Retrofit) : PopularPhotoService {
        return retrofit.create(PopularPhotoService::class.java)
    }
}