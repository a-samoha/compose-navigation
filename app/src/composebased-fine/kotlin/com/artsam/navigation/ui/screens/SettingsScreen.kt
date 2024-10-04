package com.artsam.navigation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.artsam.navigation.R
import com.artsam.navigation.ui.AppScreen
import com.artsam.navigation.ui.AppScreenEnvironment

val SettingsScreenProducer = { SettingsScreen() }

class SettingsScreen : AppScreen {
    override val environment = AppScreenEnvironment().apply {
        titleRes = R.string.settings
        icon = Icons.Default.Settings
    }

    @Composable
    override fun Content() {
        Text(
            text = "Settings Screen",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}