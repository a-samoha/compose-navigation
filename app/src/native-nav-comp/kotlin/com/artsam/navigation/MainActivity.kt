package com.artsam.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.artsam.navigation.ui.scaffold.AppToolbar
import com.artsam.navigation.ui.scaffold.NavigateUpAction
import com.artsam.navigation.ui.screens.AddItemRoute
import com.artsam.navigation.ui.screens.EditItemRoute
import com.artsam.navigation.ui.screens.ItemsRoute
import com.artsam.navigation.ui.screens.LocalNavController
import com.artsam.navigation.ui.screens.add.AddItemScreen
import com.artsam.navigation.ui.screens.edit.EditItemScreen
import com.artsam.navigation.ui.screens.items.ItemsScreen
import com.artsam.navigation.ui.screens.routeClass
import com.artsam.navigation.ui.theme.NavigationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Приклад, як можна отримувати об'єкти з Hilt:
     *
     * @Inject
     * lateinit var itemsRepo: ItemsRepository
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationTheme {
                NavApp()
            }
        }
    }
}

@Composable
fun NavApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val titleRes = when (currentBackStackEntry.routeClass()) {
        ItemsRoute::class -> R.string.items
        AddItemRoute::class -> R.string.add_item
        EditItemRoute::class -> R.string.edit_item
        else -> R.string.app_name
    }
    Scaffold(
        topBar = {
            AppToolbar(
                titleRes = titleRes,
                navigateUpAction = if (navController.previousBackStackEntry == null) {
                    NavigateUpAction.Hidden
                } else NavigateUpAction.Visible(
                    onClick = { navController.navigateUp() }
                ),
            )
        },
        floatingActionButton = {
            if (currentBackStackEntry.routeClass() == ItemsRoute::class) {
                FloatingActionButton(onClick = { navController.navigate(AddItemRoute) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = ItemsRoute,
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                composable<ItemsRoute> { ItemsScreen() }
                composable<AddItemRoute> { AddItemScreen() }
                composable<EditItemRoute> { entry ->
                    /**
                     * Витягаємо аргументи які хочемо передати до наступного Екрану.
                     * з Route
                     */
                    val route: EditItemRoute = entry.toRoute()

                    EditItemScreen(index = route.index)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavAppPreview() {
    NavigationTheme {
        NavApp()
    }
}