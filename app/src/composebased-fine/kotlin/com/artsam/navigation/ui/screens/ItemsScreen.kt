package com.artsam.navigation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artsam.navigation.ItemsRepository
import com.artsam.navigation.R
import com.artsam.navigation.ui.AppRoute
import com.artsam.navigation.ui.AppScreen
import com.artsam.navigation.ui.AppScreenEnvironment
import com.artsam.navigation.ui.AppToolbarMenuItem
import com.artsam.navigation.ui.FloatingAction
import com.asamoha.navigation.LocalRouter
import com.asamoha.navigation.Router

val ItemsScreenProducer = { ItemsScreen() }

class ItemsScreen : AppScreen {

    private var router: Router? = null
    private val itemsRepository = ItemsRepository.get()

    override val environment = AppScreenEnvironment().apply {

        titleRes = R.string.items
        icon = Icons.AutoMirrored.Filled.List
        toolbarMenuItems = listOf(
            AppToolbarMenuItem(
                titleRes = R.string.about,
                onClick = { ctx ->
                    Toast.makeText(
                        ctx,
                        ctx.resources.getString(
                            R.string.toast_from,
                            ctx.resources.getString(R.string.items)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ),
            AppToolbarMenuItem(
                titleRes = R.string.clear,
                onClick = { _ -> itemsRepository.clear() }
            ),
        )
        floatingAction = FloatingAction(
            icon = Icons.Default.Add,
            onClick = { router?.launch(AppRoute.Item(ItemScreenArgs.Add)) }
        )
    }

    @Composable
    override fun Content() {
        router = LocalRouter.current
        val items by itemsRepository.getItems().collectAsStateWithLifecycle()
        val isEmpty by remember {
            derivedStateOf { items.isEmpty() }
        }
        ItemsContent(
            isItemsEmpty = isEmpty,
            items = { items },
            onItemClicked = { index ->
                router?.launch(AppRoute.Item(ItemScreenArgs.Edit(index)))
            }
        )
    }
}

@Composable
fun ItemsContent(
    isItemsEmpty: Boolean,
    items: () -> List<String>,  // використовуємо лямбду щоб функція вважалась skippable !!!
    onItemClicked: (Int) -> Unit,
) {
    if (isItemsEmpty) {
        Text(
            text = stringResource(R.string.no_items),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val itemsList = items()
            items(itemsList.size) { index ->
                Text(
                    text = itemsList[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClicked(index)
                        }
                        .padding(all = 8.dp)
                )
            }
        }
    }
}

/**
 * [ItemsContentPreview]
 * - Альтернативна точкa входу для Preview
 * - НЕ потребує створювати фейкові репозиторії, роутер і т.д.++++++++++++
 */
@Preview(showSystemUi = true)
@Composable
private fun ItemsContentPreview() {
    ItemsContent(
        isItemsEmpty = false,
        items = { List(10) { "Item #${it + 1}" } },
        onItemClicked = {}
    )
}