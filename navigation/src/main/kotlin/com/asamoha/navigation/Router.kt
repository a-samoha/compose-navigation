package com.asamoha.navigation

import androidx.compose.runtime.Stable

/**
 * Controller for executing nav actions: launch, pop and restart.
 */
@Stable
interface Router {

    /**
     * Launch a new screen anf place it at the top of the screen stack.
     */
    fun launch(route: Route)

    /**
     * Close the current screen and go to the previous one.
     */
    fun pop(response: Any? = null)

    /**
     * remove all screens from the navigation stack and launch
     * the specified [route].
     */
    fun restart(route: Route)
}