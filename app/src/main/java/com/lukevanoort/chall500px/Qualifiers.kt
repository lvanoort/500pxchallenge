package com.lukevanoort.chall500px

import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UIScheduler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IOScheduler