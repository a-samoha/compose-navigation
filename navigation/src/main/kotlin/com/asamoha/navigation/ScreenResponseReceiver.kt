package com.asamoha.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.asamoha.navigation.internal.EmptyScreenResponseReceiver
import kotlin.reflect.KClass

val LocalScreenResponseReceiver =
    staticCompositionLocalOf<ScreenResponseReceiver> {
        EmptyScreenResponseReceiver
    }

interface ScreenResponseReceiver {

    fun <T : Any> get(clazz: KClass<T>): T?
}

@Composable
inline fun <reified T : Any> ResponseListener(
    noinline block: (T) -> Unit
) {
    val receiver = LocalScreenResponseReceiver.current
    LaunchedEffect(Unit) {
        receiver.get(T::class)?.apply(block)
    }
}