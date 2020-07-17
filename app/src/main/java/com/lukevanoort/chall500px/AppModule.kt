package com.lukevanoort.chall500px

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

@Module
class AppModule(private val app: ChallengeApplication) {
    @get:Provides
    @get:AppContext
    val applicationContext : Context
        get() = app

    @get:Provides
    val application : Application
        get() = app

    @get:Provides
    @get:IOScheduler
    val ioScheduler : Scheduler
        get() = Schedulers.io()

    @get:Provides
    @get:UIScheduler
    val uiScheduler : Scheduler
        get() = AndroidSchedulers.mainThread()
}