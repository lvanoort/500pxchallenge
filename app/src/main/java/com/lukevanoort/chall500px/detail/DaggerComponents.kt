package com.lukevanoort.chall500px.detail

import com.lukevanoort.chall500px.ViewScope
import dagger.Subcomponent


@ViewScope
@Subcomponent
interface DetailViewComponent {
    fun inject(v: DetailView)
}