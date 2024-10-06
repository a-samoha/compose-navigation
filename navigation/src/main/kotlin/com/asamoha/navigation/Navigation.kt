package com.asamoha.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.platform.LocalContext
import com.asamoha.navigation.deeplinks.DeepLinkHandler
import com.asamoha.navigation.deeplinks.MultistackState
import com.asamoha.navigation.deeplinks.StackState
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
    deepLinkHandler: DeepLinkHandler = DeepLinkHandler.DEFAULT,
): Navigation {
    val activity = LocalContext.current as? Activity
    val screenStack = rememberSaveable(rootRoutes) {
        val inputState = MultistackState(
            activeStackIndex = initialIndex,
            stacks = rootRoutes.map { rootRoute -> StackState(listOf(rootRoute)) }
        )

        val outputState = activity?.intent?.data?.let { deepLinkUri ->
            deepLinkHandler.handleDeeplink(deepLinkUri, inputState)
        } ?: inputState

        ScreenMultiStack(
            initialIndex = outputState.activeStackIndex,
            stacks = outputState.stacks.map { stackState ->
                ScreenStack(stackState.routes)
            }.toMutableStateList(),
        )
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