package com.lukevanoort.chall500px.navigation

import com.lukevanoort.chall500px.ViewScope
import com.lukevanoort.chall500px.gallery.GalleryDisplayView
import dagger.Subcomponent

@ViewScope
@Subcomponent
interface NavigationViewComponent {
    fun inject(v: NavigationView)
}