package com.artsam.navigation.ui.screens

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/**
 * Before version 2.8.0 we use strings
 */
//const val ItemsRoute = "items"
//const val AddItemRoute = "add-item"

/**
 * After version 2.8.0 we can deliver arguments thru the nav component library
 */
@Serializable
data object ItemsRoute

@Serializable
data object AddItemRoute

@Serializable
data class EditItemRoute(
    val index: Int,
)

// ----

/**
 * З версії 2.8.0 у якості маршрутів можуть виступати не лише Strings
 * а і цілі класи
 *
 * Дана функція з [NavBackStackEntry] дістає
 * саме клас маршруту навігації а НЕ Strings
 *
 * Під капотом бібліотека NavComponent перетворює
 * клас маршруту навігації в текстовий рядок
 * а, тут ми робимо обернену операцію:
 *  - перетворюємо текстовий рядок в KClass
 */
fun NavBackStackEntry?.routeClass(): KClass<*>? {
    return this?.destination?.route
        ?.split("/")
        ?.first()
        ?.let { Class.forName(it) }
        ?.kotlin
}