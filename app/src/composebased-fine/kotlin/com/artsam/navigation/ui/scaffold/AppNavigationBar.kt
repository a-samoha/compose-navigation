package com.artsam.navigation.ui.scaffold

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.artsam.navigation.ui.AppRoute
import kotlinx.collections.immutable.persistentListOf

/**
 * List of all root tabs.
 */
val RootTabs = persistentListOf(AppRoute.Tab.Items, AppRoute.Tab.Settings, AppRoute.Tab.Profile)

/**
 * In-app bottom navigation bar.
 */
@Composable
fun AppNavigationBar(
    currentIndex: Int,
    onIndexSelected: (Int) -> Unit
) {
    NavigationBar {
        RootTabs.forEachIndexed { index, tab ->
            val environment = remember(tab) {
                tab.screenProducer().environment
            }
            val icon = environment.icon
            if (icon != null) {
                NavigationBarItem(
                    selected = currentIndex == index,
                    label = { Text(stringResource(environment.titleRes)) },
                    onClick = {
                        onIndexSelected(index)
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(environment.titleRes),
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AppNavigationBarPreview() {
    AppNavigationBar(
        currentIndex = 0,
        onIndexSelected = {}
    )
}