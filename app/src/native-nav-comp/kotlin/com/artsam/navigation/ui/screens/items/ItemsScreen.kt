package com.artsam.navigation.ui.screens.items

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.artsam.navigation.R
import com.artsam.navigation.ui.screens.EditItemRoute
import com.artsam.navigation.ui.screens.LocalNavController
import com.artsam.navigation.ui.screens.items.ItemsViewModel.ScreenState

@Composable
fun ItemsScreen() {
    val viewModel: ItemsViewModel = hiltViewModel()
    val screenState = viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    ItemsContent(
        getScreenState = { screenState.value },
        onItemClick = { index ->
            navController.navigate(EditItemRoute(index))
        },
    )
}

@Composable
fun ItemsContent(
    getScreenState: () -> ScreenState,
    onItemClick: (Int) -> Unit,
) {
    val ctx = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        when (val screenState = getScreenState()) {
            ScreenState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ScreenState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(screenState.items) { index, item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .clickable { onItemClick(index) }
                                .fillMaxWidth()
                                .padding(12.dp),
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
    }
}

@Preview(showSystemUi = true)
@Composable
fun ItemsPreview() {
    ItemsContent(
        getScreenState = { ScreenState.Error("") },
        onItemClick = {},
    )
}