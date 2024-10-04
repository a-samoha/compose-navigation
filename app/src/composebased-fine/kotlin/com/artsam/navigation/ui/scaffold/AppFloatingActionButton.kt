package com.artsam.navigation.ui.scaffold

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artsam.navigation.ui.FloatingAction

@Composable
fun AppFloatingActionButton(
    floatingAction: FloatingAction?,
    modifier: Modifier = Modifier,
) {
    if (floatingAction != null) {
        FloatingActionButton(
            modifier = modifier,
            onClick = floatingAction.onClick
        ) {
            Icon(
                imageVector = floatingAction.icon,
                contentDescription = null,
            )
        }
    }
}