package com.lukevanoort.chall500px

import android.app.Application
import android.content.Context

class ChallengeApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
            .also { it.inject(this) }
    }
}

fun Context.getAppComponent(): AppComponent {
    return (this.applicationContext as ChallengeApplication).appComponent
}