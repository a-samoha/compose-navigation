package com.artsam.navigation.ui.screens.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artsam.navigation.R
import com.artsam.navigation.ui.screens.AddItemRoute
import com.artsam.navigation.ui.screens.EventConsumer
import com.artsam.navigation.ui.screens.LocalNavController

@Composable
fun AddItemScreen() {
    val viewModel: AddItemViewModel = hiltViewModel()
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    AddItemContent(
        screenState = screenState,
        onAddItemClick = { viewModel.add(it) }, // (viewModel::Add)
    )
    val navController = LocalNavController.current
    EventConsumer(viewModel.exitChannel) {
        if (navController.currentBackStackEntry?.destination?.route == AddItemRoute) {
            navController.popBackStack()
        }
    }
}

@Composable
fun AddItemContent(
    screenState: AddItemViewModel.ScreenState,
    onAddItemClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Add Item Screen",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        var itemValue by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = itemValue,
            onValueChange = { itemValue = it },
            placeholder = { Text(stringResource(R.string.enter_new_item)) },
            modifier = Modifier.padding(bottom = 16.dp),
            enabled = screenState.isTextInputEnabled,
        )
        Button(
            onClick = { onAddItemClick(itemValue) },
            enabled = screenState.isAddButtonEnabled(itemValue),
            modifier = Modifier.padding(bottom = 16.dp),
        ) {
            Text(text = stringResource(R.string.add), fontSize = 20.sp)
        }
        Box(modifier = Modifier.size(48.dp)) {
            if (screenState.isProgressVisible)
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddItemPreview() {
    AddItemContent(
        screenState = AddItemViewModel.ScreenState(),
        onAddItemClick = {}
    )
}