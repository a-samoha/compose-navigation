package com.artsam.navigation.ui.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artsam.navigation.MainViewModel
import com.artsam.navigation.di.injectViewModel
import com.artsam.navigation.ui.AppDeepLinkHandler
import com.artsam.navigation.ui.AppScreenEnvironment
import com.asamoha.navigation.NavigationHost
import com.asamoha.navigation.NavigationState
import com.asamoha.navigation.Router
import com.asamoha.navigation.rememberNavigation

/**
 * App UI skeleton containing tap toolbar, bottom nav-bar,
 * floatingaction button and screen content.
 */
@Composable
fun AppScaffold() {
    val viewModel = injectViewModel<MainViewModel>()
    val navigation = rememberNavigation(RootTabs, deepLinkHandler = AppDeepLinkHandler)
    val (router: Router, navigationState: NavigationState) = navigation
    val environment = navigationState.currentScreen.environment as AppScreenEnvironment
    Scaffold(
        topBar = {
            AppToolbar(
                titleRes = environment.titleRes,
                isRoot = navigationState.isRoot,
                menuItems = environment.toolbarMenuItems,
                onPopAction = router::pop,
                onClearAction = viewModel::clear
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(floatingAction = environment.floatingAction)
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            AppNavigationBar(
                currentIndex = navigationState.currentStackIndex,
                onIndexSelected = router::switchStack,
            )
        },
        content = { paddingValues ->
            NavigationHost(
                navigation = navigation,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}
