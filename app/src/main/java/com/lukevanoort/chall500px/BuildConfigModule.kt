package com.lukevanoort.chall500px

import com.lukevanoort.chall500px.gallery.GalleryImageLoaderConfig
import com.lukevanoort.chall500px.gallery.GalleryRepositoryConfig
import dagger.Module
import dagger.Provides

@Module
class BuildConfigModule {
    @get:Provides
    val galleryLoaderConfig: GalleryImageLoaderConfig
        get() = if (BuildConfig.USE_MOCK_GALLERY_LOADER) {
            GalleryImageLoaderConfig.Mock
        } else {
            GalleryImageLoaderConfig.Live
        }

    @get:Provides
    val galleryRepositoryConfig: GalleryRepositoryConfig
        get() = if(BuildConfig.USE_MOCK_GALLERY_REPOSITORY) {
            GalleryRepositoryConfig.Mock
        } else {
            GalleryRepositoryConfig.Live
        }
}