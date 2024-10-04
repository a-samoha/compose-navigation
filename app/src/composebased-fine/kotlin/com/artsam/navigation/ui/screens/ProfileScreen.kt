package com.artsam.navigation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.artsam.navigation.R
import com.artsam.navigation.ui.AppScreen
import com.artsam.navigation.ui.AppScreenEnvironment
import com.artsam.navigation.ui.AppToolbarMenuItem

val ProfileScreenProducer = { ProfileScreen() }

class ProfileScreen : AppScreen {
    override val environment = AppScreenEnvironment().apply {
        titleRes = R.string.profile
        icon = Icons.Default.AccountBox
        toolbarMenuItems = listOf(
            AppToolbarMenuItem(
                titleRes = R.string.about,
                onClick = { ctx ->
                    Toast.makeText(
                        ctx,
                        ctx.resources.getString(
                            R.string.toast_from,
                            ctx.resources.getString(R.string.profile)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        )
    }

    @Composable
    override fun Content() {
        Text(
            text = "Profile Screen",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}