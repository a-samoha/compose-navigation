package com.artsam.navigation.ui.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artsam.navigation.ui.AppRoute
import com.asamoha.navigation.Route

@Composable
fun AppFloatingActionButton(
    currentRoute: Route,
    onLaunchingAction: (AppRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (currentRoute == AppRoute.Tab.Items) {
        FloatingActionButton(
            modifier = modifier,
            onClick = { onLaunchingAction(AppRoute.AddItem) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }
    }
}