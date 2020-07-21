package com.lukevanoort.chall500px.navigation

/**
 * BackHandler denotes a class that potentially can handle a back
 * button press by the user
 */
interface BackHandler {
    /**
     * handleBack is called when the user presses the back button and
     * performs a 'back' action if it can
     *
     * @return true if the back was handled, false if it was not
     */
    fun handleBack() : Boolean
}