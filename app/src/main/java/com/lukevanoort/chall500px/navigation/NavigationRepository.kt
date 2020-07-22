package com.lukevanoort.chall500px.navigation

import com.lukevanoort.chall500px.AppScope
import com.lukevanoort.chall500px.gallery.GalleryRepositoryState
import com.lukevanoort.chall500px.photo.Photo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

sealed class NavigationLocation {
    object Gallery : NavigationLocation()
    data class Detail(
        val id: Int
    ) : NavigationLocation()
}

interface NavigationRepository {
    fun getCurrentNavigationLocation() : Observable<NavigationLocation>

    fun navigateTo(navigationLocation: NavigationLocation)

    /**
     * goBack attempts to go to the previous navigation location
     * and returns true if it can do so, false if there is no navigation
     * location to return to
     *
     * @return true if the navigation location could go back, false otherwise
     */
    fun goBack(): Boolean
}

/**
 * TransientNavigationRepository provides an in-memory transient navigation
 * repository, thus it will survive orientation changes as long as the Application
 * stays alive, but will not survive process restarts
 */
@AppScope
class TransientNavigationRepository @Inject constructor() : NavigationRepository {
    private val subject: BehaviorSubject<List<NavigationLocation>> = BehaviorSubject.create()

    private var currentState: MutableList<NavigationLocation> = mutableListOf(
        NavigationLocation.Gallery
    )
    private val stateLock = Any()

    init{
        subject.onNext(currentState)
    }

    override fun getCurrentNavigationLocation(): Observable<NavigationLocation> = subject.map {
        it.last()
    }

    override fun navigateTo(navigationLocation: NavigationLocation) {
        synchronized(stateLock) {
                currentState.add(navigationLocation)
                currentState.toList()
        }.let { subject.onNext(it) }
    }

    override fun goBack(): Boolean {
        synchronized(stateLock) {
            if (currentState.isEmpty()) {
                return false
            } else {
                currentState.removeAt(currentState.lastIndex)
                currentState.toList()
            }
        }.let { subject.onNext(it) }

        return true
    }


}