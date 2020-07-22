package com.lukevanoort.chall500px

import com.lukevanoort.chall500px.detail.DetailAppModule
import com.lukevanoort.chall500px.gallery.GalleryAppModules
import com.lukevanoort.chall500px.navigation.NavigationAppModule
import com.lukevanoort.chall500px.photo.PhotoModule
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        GalleryAppModules::class,
        NavigationAppModule::class,
        DetailAppModule::class,
        PhotoModule::class,
        BuildConfigModule::class
    ]
)
interface AppComponent {
    fun inject(app: ChallengeApplication)

    fun plusActivity(activityModule: ActivityModule): ActivityComponent
}