package com.lukevanoort.chall500px

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import com.lukevanoort.chall500px.detail.DetailViewComponent
import com.lukevanoort.chall500px.gallery.GalleryViewComponent
import com.lukevanoort.chall500px.navigation.NavigationViewComponent
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(act: ChallengeActivity)

    fun galleryViewComponent(): GalleryViewComponent
    fun navigationViewComponent(): NavigationViewComponent
    fun detailViewComponent(): DetailViewComponent
}

interface ActivityComponentProvider {
    fun provideActivityComponent(): ActivityComponent
}


fun View.getActivityComponent(): ActivityComponent? {
    val act = this.getActivity()
    return if (act is ActivityComponentProvider) {
        act.provideActivityComponent()
    } else {
        null
    }
}