package com.artsam.navigation.ui.screens.edit

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.artsam.navigation.R
import com.artsam.navigation.ui.screens.EditItemRoute
import com.artsam.navigation.ui.screens.EventConsumer
import com.artsam.navigation.ui.screens.LocalNavController
import com.artsam.navigation.ui.screens.edit.EditItemViewModel.ScreenState
import com.artsam.navigation.ui.screens.routeClass

@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    val navController = LocalNavController.current
    EventConsumer(channel = viewModel.exitChannel) {
        if (navController.currentBackStackEntry.routeClass() == EditItemRoute::class) {
            navController.popBackStack()
        }
    }
    val screenState by viewModel.stateFlow.collectAsState()
    EditItemContent(
        state = screenState,
        onEditButtonClicked = viewModel::update,
    )
}

@Composable
fun EditItemContent(
    state: ScreenState,
    onEditButtonClicked: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (state) {
            ScreenState.Loading -> CircularProgressIndicator()
            is ScreenState.Success -> SuccessEditItemContent(state, onEditButtonClicked)
        }

    }
}

@Composable
private fun SuccessEditItemContent(
    state: ScreenState.Success,
    onEditButtonClicked: (String) -> Unit
) {
    var itemValue by rememberSaveable { mutableStateOf(state.loadedItem) }
    OutlinedTextField(
        value = itemValue,
        onValueChange = { itemValue = it },
        placeholder = { Text(stringResource(R.string.edit_item_title)) },
        modifier = Modifier.padding(bottom = 16.dp),
        enabled = !state.isEditInProgress,
    )
    Button(
        onClick = { onEditButtonClicked(itemValue) },
        enabled = itemValue.isNotBlank() && !state.isEditInProgress,
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Text(text = stringResource(R.string.accept), fontSize = 20.sp)
    }
    Box(modifier = Modifier.size(48.dp)) {
        if (state.isEditInProgress)
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}