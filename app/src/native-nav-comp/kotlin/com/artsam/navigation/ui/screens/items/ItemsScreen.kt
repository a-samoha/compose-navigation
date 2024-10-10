package com.artsam.navigation.ui.screens.items

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.artsam.navigation.R
import com.artsam.navigation.ui.screens.AddItemRoute
import com.artsam.navigation.ui.screens.LocalNavController
import com.artsam.navigation.ui.screens.items.ItemsViewModel.ScreenState

@Composable
fun ItemsScreen() {
    val viewModel: ItemsViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.uiState.collectAsState()
    ItemsContent(
        getScreenState = { screenState.value },
        onLaunchAddItemScreen = { navController.navigate(AddItemRoute) }
    )
}

@Composable
fun ItemsContent(
    getScreenState: () -> ScreenState,
    onLaunchAddItemScreen: () -> Unit
) {
    val ctx = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        when (val screenState = getScreenState()) {
            ScreenState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ScreenState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(screenState.items) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }
            }
            is ScreenState.Error -> Toast.makeText(
                ctx,
                stringResource(R.string.loading_error),
                Toast.LENGTH_SHORT
            ).show()
        }
        FloatingActionButton(
            onClick = onLaunchAddItemScreen,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ItemsPreview() {
    ItemsContent(
        getScreenState = { ScreenState.Error("") },
        onLaunchAddItemScreen = { }
    )
}