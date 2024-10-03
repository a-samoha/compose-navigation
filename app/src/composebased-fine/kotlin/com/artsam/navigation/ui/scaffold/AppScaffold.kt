package com.artsam.navigation.ui.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artsam.navigation.ItemsRepository
import com.artsam.navigation.R
import com.artsam.navigation.ui.AppRoute
import com.artsam.navigation.ui.screens.AddItemScreen
import com.artsam.navigation.ui.screens.ItemsScreen
import com.artsam.navigation.ui.screens.ProfileScreen
import com.artsam.navigation.ui.screens.SettingsScreen
import com.asamoha.navigation.NavigationHost
import com.asamoha.navigation.NavigationState
import com.asamoha.navigation.Router
import com.asamoha.navigation.rememberNavigation

@Composable
fun AppScaffold() {
    val navigation = rememberNavigation(initialRoute = AppRoute.Tab.Items)
    val (router: Router, navigationState: NavigationState) = navigation

    Scaffold(
        topBar = {
            AppToolbar(
                titleRes = (navigationState.currentRoute as? AppRoute)?.titleRes
                    ?: R.string.app_name,
                isRoot = navigationState.isRoot,
                onPopAction = router::pop,
                onClearAction = ItemsRepository.get()::clear
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                currentRoute = navigationState.currentRoute,
                onLaunchingAction = { router.launch(it) })
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
            ) { currentRoute ->
                when (currentRoute) {
                    AppRoute.Tab.Items -> ItemsScreen()
                    AppRoute.Tab.Settings -> SettingsScreen()
                    AppRoute.Tab.Profile -> ProfileScreen()
                    AppRoute.AddItem -> AddItemScreen()
                }
            }
        }
    )
}
