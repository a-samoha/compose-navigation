package com.asamoha.navigation.internal

import com.asamoha.navigation.ScreenResponseReceiver
import kotlin.reflect.KClass

internal object EmptyScreenResponseReceiver: ScreenResponseReceiver {
    override fun <T : Any> get(clazz: KClass<T>): T? = null
}