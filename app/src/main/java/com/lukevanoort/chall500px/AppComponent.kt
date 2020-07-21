package com.lukevanoort.chall500px

import com.lukevanoort.chall500px.gallery.GalleryAppModules
import com.lukevanoort.chall500px.navigation.NavigationAppModule
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class,
        GalleryAppModules::class,
        NavigationAppModule::class,
        BuildConfigModule::class
    ]
)
interface AppComponent {
    fun inject(app: ChallengeApplication)

    fun plusActivity(activityModule: ActivityModule): ActivityComponent
}