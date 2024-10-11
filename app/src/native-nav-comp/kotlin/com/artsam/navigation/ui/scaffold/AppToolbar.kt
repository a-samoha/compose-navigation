package com.artsam.navigation.ui.scaffold

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.artsam.navigation.ui.scaffold.NavigateUpAction.Visible

sealed class NavigateUpAction {
    data object Hidden : NavigateUpAction()
    data class Visible(
        val onClick: () -> Unit,
    ) : NavigateUpAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    @StringRes titleRes: Int,
    navigateUpAction: NavigateUpAction, //(() -> Unit)?,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (navigateUpAction is Visible) {
                IconButton(
                    onClick = navigateUpAction.onClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Text(text = stringResource(titleRes))
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}