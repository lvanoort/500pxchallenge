package com.lukevanoort.chall500px

import dagger.Component
import dagger.Subcomponent

@AppScope
@Component(
    modules = [AppModule::class]
)
interface AppComponent {
    fun inject(app: ChallengeApplication)

    fun plusActivity(activityModule: ActivityModule): ActivityComponent
}