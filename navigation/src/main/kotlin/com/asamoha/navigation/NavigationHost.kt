package com.asamoha.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asamoha.navigation.internal.EmptyRouter
import com.asamoha.navigation.internal.InternalNavigationState
import com.asamoha.navigation.internal.NavigationEvent
import com.asamoha.navigation.viewmodel.ScreenViewModelStoreOwner
import com.asamoha.navigation.viewmodel.ScreenViewModelStoreProvider
import kotlinx.coroutines.flow.filterIsInstance

/**
 * Спостерігає коли який екран створюється/знищується.
 *
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
     * функція [viewModel] завжди повертає один і той самий екземпляр
     * ScreenViewModelStoreProvider для Main Activity
     */
    val viewModelStoreProvider = viewModel<ScreenViewModelStoreProvider>()

    /**
     * Оскільки ми використовуємо ключ для remember
     * то як тільки зміниться екран, то зміниться і viewModelStoreOwner
     */
    val viewModelStoreOwner = remember(internalState.currentUuid) {
        ScreenViewModelStoreOwner(viewModelStoreProvider, internalState.currentUuid)
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
                LocalViewModelStoreOwner provides viewModelStoreOwner, // підміняємо нативну реалізацію кастомним viewModelStoreOwner
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
                viewModelStoreProvider.removeStore(event.routeRecord.uuid)
            }
    }
}