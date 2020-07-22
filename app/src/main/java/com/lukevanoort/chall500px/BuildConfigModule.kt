package com.lukevanoort.chall500px

import com.lukevanoort.chall500px.detail.DetailRepositoryConfig
import com.lukevanoort.chall500px.gallery.GalleryRepositoryConfig
import com.lukevanoort.chall500px.photo.PhotoLoaderConfig
import dagger.Module
import dagger.Provides

@Module
class BuildConfigModule {
    @get:Provides
    val galleryLoaderConfig: PhotoLoaderConfig
        get() = if (BuildConfig.USE_MOCK_PHOTO_LOADER) {
            PhotoLoaderConfig.Mock
        } else {
            PhotoLoaderConfig.Live
        }

    @get:Provides
    val galleryRepositoryConfig: GalleryRepositoryConfig
        get() = if(BuildConfig.USE_MOCK_GALLERY_REPOSITORY) {
            GalleryRepositoryConfig.Mock
        } else {
            GalleryRepositoryConfig.Live
        }

    @get:Provides
    val detailRepositoryConfig: DetailRepositoryConfig
        get() = if(BuildConfig.USE_MOCK_DETAIL_REPOSITORY) {
            DetailRepositoryConfig.Mock
        } else {
            DetailRepositoryConfig.Live
        }

    @get:Provides
    @get:ApiRootUrl
    val apiRootUrl: String
        get() = BuildConfig.API_ROOT

    @get:Provides
    @get:ApiConsumerKey
    val apiConsumerKey: String
        get() = BuildConfig.API_CONSUMER_KEY
}