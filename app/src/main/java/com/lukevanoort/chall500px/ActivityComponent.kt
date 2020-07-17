package com.lukevanoort.chall500px

import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(act: ChallengeActivity)
}