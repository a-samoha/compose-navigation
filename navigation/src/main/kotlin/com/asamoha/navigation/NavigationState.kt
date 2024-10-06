package com.asamoha.navigation

import androidx.compose.runtime.Stable

/**
 * Represents the current state of navigation
 */
@Stable
interface NavigationState {

    /**
     * Whether there is only one screen in the stack.
     */
    val isRoot: Boolean

    /**
     * Current visible to the user screen
     * (which is located at the top of screen stack).
     *
     * Analog Intent for Activity
     */
    val currentRoute: Route

    /**
     * Analog Activity which starts with Intent
     */
    val currentScreen: Screen

    val currentStackIndex: Int
}