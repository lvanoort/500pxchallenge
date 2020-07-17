package com.lukevanoort.chall500px.gallery

import com.lukevanoort.chall500px.ViewScope
import dagger.Subcomponent


@ViewScope
@Subcomponent
interface GalleryViewComponent {
    fun inject(v: GalleryDisplayView)
}