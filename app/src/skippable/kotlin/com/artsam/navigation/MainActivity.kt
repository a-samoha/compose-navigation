package com.artsam.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artsam.mylibrary.LabelFromAnotherModule
import java.util.UUID

/**
 * Даний клас є стабільним, тому що:
 * - УСІ аргументи є 'val'
 * - УСІ аргументи є стабільні (примітиви або String)
 */
data class Label(
    val text: String
)

/**
 * Даний клас є НЕ стабільним, тому що:
 * - аргумент є 'var'
 */
data class UnstLabel(
    var text: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }
}

@Composable
fun AppScreen() {
    var label by remember { mutableStateOf(LabelFromAnotherModule("Hello")) }
    var counter by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        LabelFromAnotherModuleText(label)
        CounterText(counter)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { counter++ }
        ) {
            Text(text = stringResource(R.string.increment), fontSize = 18.sp)
        }
        Button(
            onClick = { label = LabelFromAnotherModule(UUID.randomUUID().toString().substringBefore("-")) }
        ) {
            Text(text = stringResource(R.string.random_label), fontSize = 18.sp)
        }
    }
}

/**
 * Якщо Composable функція приймає в аргументи
 * примітивні типи або String
 * така функція вважається `skippable` !!!
 */
@Composable
fun CounterText(counter: Any) {
    Text(
        text = "$counter",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
    )
}

/**
 * Тут Composable функція приймає
 * Стабільний клас [Label]
 * така функція вважається Stable !!!
 */
@Composable
fun LabelText(label: Label) {
    Text(
        text = label.text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
    )
}

/**
 * Тут Composable приймає
 * НЕ стабільний клас [UnstLabel]
 * така функція вважається `not skippable` !!!
 */
@Composable
fun UnstLabelText(label: UnstLabel) {
    Text(
        text = label.text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
    )
}

/**
 * Тут Composable приймає
 * стабільний клас [LabelFromAnotherModule]
 * АЛЕ вважає, що всі класи з інших модулів НЕ стабільні за замовчуванням
 *
 * тому треба додати для [LabelFromAnotherModule] анотацію @Stable
 * з пакету implementation("androidx.compose.runtime:runtime:1.6.0")
 *
 * тоді функція буде вважаєтис `skippable` !!!
 */
@Composable
fun LabelFromAnotherModuleText(label: LabelFromAnotherModule) {
    Text(
        text = label.text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
    )
}


