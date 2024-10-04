package com.artsam.navigation.ui

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.artsam.navigation.R
import com.asamoha.navigation.Screen
import com.asamoha.navigation.ScreenEnvironment

@Stable
interface AppScreen : Screen {

    override val environment: AppScreenEnvironment
}

@Stable
class AppScreenEnvironment : ScreenEnvironment {
    var titleRes: Int by mutableIntStateOf(R.string.app_name)
    var icon: ImageVector? by mutableStateOf(null)
    var toolbarMenuItems: List<AppToolbarMenuItem>? by mutableStateOf(null)
    var floatingAction: FloatingAction? by mutableStateOf(null)
}

@Immutable
data class AppToolbarMenuItem(
    val titleRes: Int,
    val icon: ImageVector? = null,
    val onClick: (Context) -> Unit,
)

@Immutable
data class FloatingAction(
    val icon: ImageVector,
    val onClick: () -> Unit,
)