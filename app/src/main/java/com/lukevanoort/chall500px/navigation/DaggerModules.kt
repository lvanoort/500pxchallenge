package com.lukevanoort.chall500px.navigation

import dagger.Module
import dagger.Provides

@Module
class NavigationAppModule {
    @Provides
    fun provideNavigationRepository(
        impl: TransientNavigationRepository
    ) : NavigationRepository = impl

    @Provides
    fun provideNavigationViewModel(
        impl: NavigationViewModelImpl
    ) : NavigationViewModel {
        return impl
    }
}