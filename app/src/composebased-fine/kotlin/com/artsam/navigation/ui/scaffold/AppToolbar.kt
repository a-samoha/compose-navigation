package com.artsam.navigation.ui.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.artsam.navigation.R
import com.artsam.navigation.ui.AppToolbarMenuItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    titleRes: Int,
    isRoot: Boolean,
    menuItems: List<AppToolbarMenuItem>?,
    onPopAction: () -> Unit,
    onClearAction: () -> Unit,
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!isRoot) onPopAction()
                }
            ) {
                Icon(
                    imageVector = if (isRoot)
                        Icons.Default.Menu
                    else
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        actions = {
            var showPopupMenu by remember { mutableStateOf(false) }
            val context = LocalContext.current
            IconButton(
                onClick = { showPopupMenu = true }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )
                DropdownMenu(
                    expanded = showPopupMenu,
                    onDismissRequest = { showPopupMenu = false }
                ) {
                    menuItems?.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(stringResource(item.titleRes)) },
                            leadingIcon = if (item.icon != null) {
                                {
                                    Icon(
                                        item.icon,
                                        contentDescription = null,
                                    )
                                }
                            } else null,
                            onClick = {
                                if (item.titleRes == R.string.clear) {
                                    onClearAction()
                                } else {
                                    item.onClick(context)
                                }
                                showPopupMenu = false
                            }
                        )
                    }
                }
            }
        }
    )
}
