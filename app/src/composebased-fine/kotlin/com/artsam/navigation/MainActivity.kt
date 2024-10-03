package com.artsam.navigation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val items by itemsRepository.getItems().collectAsStateWithLifecycle()

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
                    AppRoute.Tab.Items -> ItemsScreen(items)
                    AppRoute.Tab.Settings -> SettingsScreen()
                    AppRoute.Tab.Profile -> ProfileScreen()
                    AppRoute.AddItem ->
                        AddItemScreen(
                            onSubmitNewItem = {
                                itemsRepository.addItem(it)
                                router.pop()
                            }
                        )
                }
            }
        }
    )
}

@Composable
fun ItemsScreen(items: List<String>) {
    if (items.isEmpty()) {
        Text(
            text = stringResource(R.string.no_items),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
        )
    } else {
        LazyColumn {
            items(items) { item ->
                Text(
                    text = item,
                    modifier = Modifier.padding(all = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Text(
        text = "Settings Screen",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
    )
}

@Composable
fun ProfileScreen() {
    Text(
        text = "Profile Screen",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
    )
}

@Composable
fun AddItemScreen(onSubmitNewItem: (String) -> Unit) {
    var newItemValue by remember { mutableStateOf("") }
    val isAddEnabled by remember {
        derivedStateOf { newItemValue.isNotEmpty() }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedTextField(
            value = newItemValue,
            label = { Text(stringResource(R.string.enter_value)) },
            singleLine = true,
            onValueChange = { newValue ->
                newItemValue = newValue
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = isAddEnabled,
            onClick = { onSubmitNewItem(newItemValue) }
        ) {
            Text(text = stringResource(R.string.add_new_item))
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AppScreenPreview() {
    AppScreen()
}