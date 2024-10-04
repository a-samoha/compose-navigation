package com.artsam.navigation.ui.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artsam.navigation.ui.AppRoute
import com.artsam.navigation.ui.AppScreenEnvironment
import com.asamoha.navigation.NavigationHost
import com.asamoha.navigation.NavigationState
import com.asamoha.navigation.Router
import com.asamoha.navigation.rememberNavigation

@Composable
fun AppScaffold() {
    val navigation = rememberNavigation(initialRoute = AppRoute.Tab.Items)
    val (router: Router, navigationState: NavigationState) = navigation
    val environment = navigationState.currentScreen.environment as AppScreenEnvironment
    Scaffold(
        topBar = {
            AppToolbar(
                titleRes = environment.titleRes,
                isRoot = navigationState.isRoot,
                menuItems = environment.toolbarMenuItems,
                onPopAction = router::pop,
//                onClearAction = itemsRepository::clear
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(floatingAction = environment.floatingAction)
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (navigationState.isRoot) {
                AppNavigationBar(
                    currentRoute = navigationState.currentRoute,
                    onRouteSelected = router::restart,
                )
            }
        },
        content = { paddingValues ->
            NavigationHost(
                navigation = navigation,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}
