package com.lukevanoort.chall500px

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(
    private val host: AppCompatActivity
) {
    @get:Provides
    @get:ActivityScope
    val activity: Context
        get() = host

    @get:Provides
    @get:ActivityScope
    @get:ActivityContext
    val activityContext: Context
        get() = host

    @get:ActivityScope
    @get:Provides
    val lifecycleOwner: LifecycleOwner
        get() = host
}