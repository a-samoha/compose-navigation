package com.artsam.navigation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artsam.navigation.ui.screens.AddItemScreen
import com.artsam.navigation.ui.screens.ItemsScreen
import com.artsam.navigation.ui.screens.ProfileScreen
import com.artsam.navigation.ui.screens.SettingsScreen
import com.asamoha.navigation.NavigationHost
import com.asamoha.navigation.NavigationState
import com.asamoha.navigation.Router
import com.asamoha.navigation.rememberNavigation

val RootTabs = listOf(AppRoute.Tab.Items, AppRoute.Tab.Settings, AppRoute.Tab.Profile)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(itemsRepository: ItemsRepository = ItemsRepository.get()) {
    val navigation = rememberNavigation(initialRoute = AppRoute.Tab.Items)
    val (router: Router, navigationState: NavigationState) = navigation

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            (navigationState.currentRoute as? AppRoute)?.titleRes
                                ?: R.string.app_name
                        ),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!navigationState.isRoot) router.pop()
                        }
                    ) {
                        Icon(
                            imageVector = if (navigationState.isRoot)
                                Icons.Default.Menu
                            else
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    var showPopupMenu by remember { mutableStateOf(false) }
                    val context = LocalContext.current
                    IconButton(
                        onClick = { showPopupMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )
                        DropdownMenu(
                            expanded = showPopupMenu,
                            onDismissRequest = { showPopupMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.about)) },
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        R.string.scaffold_app,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showPopupMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.clear)) },
                                onClick = {
                                    itemsRepository.clear()
                                    showPopupMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (navigationState.currentRoute == AppRoute.Tab.Items) {
                FloatingActionButton(
                    onClick = { router.launch(AppRoute.AddItem) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (navigationState.isRoot) {
                NavigationBar {
                    RootTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = navigationState.currentRoute == tab,
                            label = { Text(stringResource(tab.titleRes)) },
                            onClick = {
                                router.restart(tab)
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

@Composable
@Preview(showSystemUi = true)
fun AppScreenPreview() {
    AppScreen()
}