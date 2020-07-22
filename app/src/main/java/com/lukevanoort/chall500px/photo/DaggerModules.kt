package com.lukevanoort.chall500px.photo

import com.lukevanoort.chall500px.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Provider


sealed class PhotoLoaderConfig {
    object Mock : PhotoLoaderConfig()
    object Live: PhotoLoaderConfig()
}

@Module
class PhotoModule {
    @Provides
    @AppScope
    fun getGalleryPhotoLoader(
        photoLoaderConfig: PhotoLoaderConfig,
        mockProvider: Provider<MockPhotoLoader>,
        coilProvider: Provider<CoilPhotoLoader>
    ) : PhotoLoader = when(photoLoaderConfig) {
        PhotoLoaderConfig.Mock -> mockProvider.get()
        PhotoLoaderConfig.Live -> coilProvider.get()
    }

    @Provides
    @AppScope
    fun providePopularPhoto(retrofit: Retrofit) : PopularPhotoService {
        return retrofit.create(PopularPhotoService::class.java)
    }

    @Provides
    @AppScope
    fun providePhotoDetail(retrofit: Retrofit) : IndividualPhotoService {
        return retrofit.create(IndividualPhotoService::class.java)
    }
}