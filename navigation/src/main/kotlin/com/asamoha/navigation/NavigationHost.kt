package com.asamoha.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.asamoha.navigation.internal.EmptyRouter
import com.asamoha.navigation.internal.InternalNavigationState
import com.asamoha.navigation.internal.NavigationEvent
import kotlinx.coroutines.flow.filterIsInstance

/**
 * We use staticCompositionLocalOf
 * because the router wont be recreated
 */
val LocalRouter = staticCompositionLocalOf<Router> { EmptyRouter() }

@Composable
fun NavigationHost(
    navigation: Navigation,
    modifier: Modifier = Modifier,
) {
    val (router: Router, navigationState: NavigationState, internalState: InternalNavigationState) = navigation

    BackHandler(enabled = !navigationState.isRoot) {
        router.pop()
    }

    /**
     * [saveableStateHolder] подовжує життевий цикл
     * всіх вкладених функцій [rememberSaveable],
     * до життевого циклу композиції де він оголошений
     * (в даному випадку [NavigationHost])
     */
    val saveableStateHolder = rememberSaveableStateHolder()
    saveableStateHolder.SaveableStateProvider(key = internalState.currentUuid) {

        Box(modifier = modifier) {
            CompositionLocalProvider(
                LocalRouter provides router,
                LocalScreenResponseReceiver provides internalState.screenResponseReceiver,
            ) {
                navigationState.currentScreen.Content()
            }
        }

    }

    LaunchedEffect(navigation) {
        navigation.internalNavigationState.listen()
            .filterIsInstance<NavigationEvent.Removed>()
            .collect { event ->
                saveableStateHolder.removeState(event.routeRecord.uuid)
            }
    }
}