package com.lukevanoort.chall500px

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

data class ApiConfig(
    val rootUrl: String,
    val consumerKey: String
)

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

    @Provides
    @AppScope
    fun provideRetrofit(
        @IOScheduler ioScheduler: Scheduler,
        @ApiRootUrl apiRoot: String
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiRoot)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(ioScheduler))
            .build()
    }
}