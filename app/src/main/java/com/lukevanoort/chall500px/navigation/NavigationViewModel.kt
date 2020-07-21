package com.lukevanoort.chall500px.navigation

import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

sealed class NavigationViewState {
    object Gallery : NavigationViewState()
    data class Detail(
        val id: Int
    ) : NavigationViewState()
}

interface NavigationViewModel {
    fun getState() : Observable<NavigationViewState>
    fun goBack(): Boolean
}

class NavigationViewModelImpl @Inject constructor(
    private val repository: NavigationRepository
) : NavigationViewModel {
    override fun getState(): Observable<NavigationViewState> =
        repository.getCurrentNavigationLocation().map {
            when (it) {
                NavigationLocation.Gallery -> {
                    NavigationViewState.Gallery
                }
                is NavigationLocation.Detail -> {
                    NavigationViewState.Detail(it.id)
                }
            }
        }

    override fun goBack(): Boolean = repository.goBack()
}

