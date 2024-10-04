package com.artsam.navigation.ui.screens

import android.os.Bundle
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
import com.artsam.navigation.ItemsRepository
import com.artsam.navigation.R
import com.artsam.navigation.ui.AppRoute
import com.artsam.navigation.ui.AppScreen
import com.artsam.navigation.ui.AppScreenEnvironment
import com.artsam.navigation.ui.AppToolbarMenuItem
import com.asamoha.navigation.LocalRouter

val AddItemScreenProducer = { AddItemScreen() }

class AddItemScreen : AppScreen {
    override val environment = AppScreenEnvironment().apply {
        titleRes = R.string.add_item
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
        val itemsRepository = ItemsRepository.get()
        val router = LocalRouter.current
        AddItemContent(
            onSubmitNewItem = {
                itemsRepository.addItem(it)
                router.pop()
            },
            onLaunchSettingsScreen = { router.launch(AppRoute.Tab.Settings) }
        )
    }
}

@Composable
fun AddItemContent(
    onSubmitNewItem: (String) -> Unit,
    onLaunchSettingsScreen: () -> Unit,
) {

    /**
     * [rememberSaveable] хоч і зберігає дані в [Bundle] але
     * НЕ преживає знащення самої композиції
     * тобто: якщо знищується (напр. коли закрив інший скрін)
     * композиція [AddItemContent] -  [newItemValue] також знищиться.
     */
    var newItemValue by rememberSaveable { mutableStateOf("") }

    val isAddEnabled by remember {
        /**
         * [derivedStateOf] Суттево зменшить кількість емітів (тригерів рекомпозиції)
         * newItemValue буде емітити на кожну нову букву, а
         * isAddEnabled буде ємітити ЛИШЕ коли поле пусте, або заповнене
         * тому що стейт НЕ емітить (ігнорує) ідентичні значення
         * напр. (true, true, true, true) в єміт полетить лише перший true !!!
         */
        derivedStateOf { newItemValue.isNotEmpty() }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedTextField(
            value = newItemValue,
            label = { Text(stringResource(R.string.enter_value)) },
            singleLine = true,
            onValueChange = { newValue ->
                newItemValue = newValue
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = isAddEnabled,
            onClick = { onSubmitNewItem(newItemValue) }
        ) {
            Text(text = stringResource(R.string.add_new_item))
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
    AddItemContent(onSubmitNewItem = {}, onLaunchSettingsScreen = {})
}

/**
 * [AddItemScreenTest]
 * - точка входу для ТЕСТІВ
 * - готує собі необхідні mocks
 * -МАЄ знаходитись в тестах !!!
 */
@Composable
private fun AddItemScreenTest() {
    AddItemContent(onSubmitNewItem = {}, onLaunchSettingsScreen = {})
}