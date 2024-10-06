package com.asamoha.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.asamoha.navigation.internal.InternalNavigationState
import com.asamoha.navigation.internal.ScreenMultiStack
import com.asamoha.navigation.internal.ScreenStack
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
fun rememberNavigation(
    rootRoutes: ImmutableList<Route>,
    initialIndex: Int = 0,
): Navigation {
    val screenStack = rememberSaveable(rootRoutes) {
        val stacks = SnapshotStateList<ScreenStack>()
        stacks.addAll(rootRoutes.map(::ScreenStack))
        ScreenMultiStack(stacks, initialIndex)
    }

    return remember(rootRoutes) {
        Navigation(
            router = screenStack,
            navigationState = screenStack,
            internalNavigationState = screenStack,
        )
    }
}

/**
 * Навігація з одним стеком це частковий випадок навігації з мультистеком
 */
@Composable
fun rememberNavigation(
    initialRoute: Route,
): Navigation = rememberNavigation(persistentListOf(initialRoute))