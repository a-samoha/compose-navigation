package com.artsam.navigation.ui.scaffold

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.artsam.navigation.ui.AppRoute
import com.asamoha.navigation.Route

val RootTabs = listOf(AppRoute.Tab.Items, AppRoute.Tab.Settings, AppRoute.Tab.Profile)

@Composable
fun AppNavigationBar(
    currentRoute: Route,
    onRouteSelected: (AppRoute.Tab) -> Unit
) {
    NavigationBar {
        RootTabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab,
                label = { Text(stringResource(tab.titleRes)) },
                onClick = {
                    onRouteSelected(tab)
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(tab.titleRes),
                    )
                }
            )
        }
    }
}