package com.asamoha.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.asamoha.navigation.internal.InternalNavigationState
import com.asamoha.navigation.internal.RouteRecord
import com.asamoha.navigation.internal.ScreenStack

/**
 * Pattern `Fasad` for whole navigation module
 *
 * Entry point to the navigation staff.
 * Use [rememberNavigation] in order to
 * create on instance of this class.
 */
@Stable
data class Navigation internal constructor(
    val router: Router,
    val navigationState: NavigationState,
    internal val internalNavigationState: InternalNavigationState,
)

/**
 * Create and remember a new [Navigation] instance.
 * @paraminitialRoute starting screen to be displayed in the [NavigationHost]
 */
@Composable
fun rememberNavigation(initialRoute: Route): Navigation {
    val screenStack = rememberSaveable {
        ScreenStack(mutableStateListOf(RouteRecord(initialRoute)))
    }

    return remember(initialRoute) {
        Navigation(
            router = screenStack,
            navigationState = screenStack,
            internalNavigationState = screenStack,
        )
    }
}