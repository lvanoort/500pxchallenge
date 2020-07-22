package com.lukevanoort.chall500px.detail

import com.lukevanoort.chall500px.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider


sealed class DetailRepositoryConfig {
    object Mock : DetailRepositoryConfig()
    object Live: DetailRepositoryConfig()
}


@Module
class DetailAppModule {
    @Provides
    fun provideDetailViewModel(impl:DetailViewModelImpl) : DetailViewModel = impl

    @Provides
    @AppScope
    fun provideDetailRepository(
        cfg: DetailRepositoryConfig,
        liveProv: Provider<LivePhotoDetailRepository>,
        mockProv: Provider<MockPhotoDetailRepository>
    ) : PhotoDetailRepository =  when(cfg) {
        DetailRepositoryConfig.Mock -> {
            mockProv.get()
        }
        DetailRepositoryConfig.Live -> {
            liveProv.get()
        }
    }
}