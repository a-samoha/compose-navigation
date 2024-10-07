package com.artsam.navigation.ui.screens.item

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artsam.navigation.R
import com.artsam.navigation.di.injectViewModel
import com.artsam.navigation.ui.AppRoute
import com.artsam.navigation.ui.AppScreen
import com.artsam.navigation.ui.AppScreenEnvironment
import com.artsam.navigation.ui.AppToolbarMenuItem
import com.asamoha.navigation.LocalRouter
import kotlinx.parcelize.Parcelize

/**
 * Caring (Керування)  -  перетворення функції з кількома аргументами
 * у функції з меншою кільуістю аргументів
 */
fun itemScreenProducer(args: ItemScreenArgs): () -> ItemScreen {
    return { ItemScreen(args) }
}

sealed class ItemScreenArgs : Parcelable {
    @Parcelize
    data object Add : ItemScreenArgs()

    @Parcelize
    data class Edit(val index: Int) : ItemScreenArgs()
}

data class ItemScreenResponse(
    val args: ItemScreenArgs,
    val newValue: String,
)

class ItemScreen(
    private val args: ItemScreenArgs,
) : AppScreen {
    override val environment = AppScreenEnvironment().apply {
        titleRes = when (args) {
            ItemScreenArgs.Add -> R.string.add_item
            is ItemScreenArgs.Edit -> R.string.edit_item
        }
        toolbarMenuItems = listOf(
            AppToolbarMenuItem(
                titleRes = R.string.about,
                onClick = { ctx ->
                    Toast.makeText(
                        ctx,
                        ctx.resources.getString(
                            R.string.toast_from,
                            ctx.resources.getString(R.string.add_item)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        )
    }

    @Composable
    override fun Content() {
        val viewModel = injectViewModel<ItemViewModel, ItemViewModel.Factory> { factory ->
            factory.create(args)
        }
        val router = LocalRouter.current
        ItemContent(
            initialValue = remember { viewModel.getInitialValue() },
            isAddMode = args is ItemScreenArgs.Add,
            onSubmitNewItem = { newValue ->
                router.pop(ItemScreenResponse(args, newValue))
            },
            onLaunchSettingsScreen = { router.launch(AppRoute.Tab.Settings) }
        )
    }
}

@Composable
fun ItemContent(
    initialValue: String = "",
    isAddMode: Boolean = false,
    onSubmitNewItem: (String) -> Unit,
    onLaunchSettingsScreen: () -> Unit,
) {

    /**
     * [rememberSaveable] хоч і зберігає дані в [Bundle] але
     * НЕ преживає знащення самої композиції
     * тобто: якщо знищується (напр. коли перекриває інший скрін)
     * композиція [ItemContent] - [initialValue] також знищиться.
     */
    var currentItemValue by rememberSaveable { mutableStateOf(initialValue) }

    val isAddEnabled by remember {
        /**
         * [derivedStateOf] Суттево зменшить кількість емітів (тригерів рекомпозиції)
         * newItemValue буде емітити на кожну нову букву, а
         * isAddEnabled буде ємітити ЛИШЕ коли поле пусте, або заповнене
         * тому що стейт НЕ емітить (ігнорує) ідентичні значення
         * напр. (true, true, true, true) в єміт полетить лише перший true !!!
         */
        derivedStateOf { currentItemValue.isNotEmpty() }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedTextField(
            value = currentItemValue,
            label = { Text(stringResource(R.string.enter_value)) },
            singleLine = true,
            onValueChange = { newValue ->
                currentItemValue = newValue
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = isAddEnabled,
            onClick = { onSubmitNewItem(currentItemValue) }
        ) {
            val btnText = if (isAddMode) R.string.add_new_item else R.string.edit_item
            Text(text = stringResource(btnText))
        }
        Button(
            enabled = true,
            onClick = onLaunchSettingsScreen
        ) {
            Text(text = "Launch Settings")
        }
    }
}

/**
 * [AddItemPreview]
 * - Альтернативна точкa входу для Preview
 * - НЕ потребує створювати фейкові репозиторії, роутер і т.д.++++++++++++
 */
@Preview(showSystemUi = true)
@Composable
private fun AddItemPreview() {
    ItemContent(onSubmitNewItem = {}, onLaunchSettingsScreen = {})
}

/**
 * [AddItemScreenTest]
 * - точка входу для ТЕСТІВ
 * - готує собі необхідні mocks
 * -МАЄ знаходитись в тестах !!!
 */
@Composable
private fun AddItemScreenTest() {
    ItemContent(onSubmitNewItem = {}, onLaunchSettingsScreen = {})
}