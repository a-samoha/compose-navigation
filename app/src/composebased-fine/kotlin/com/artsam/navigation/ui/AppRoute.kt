package com.artsam.navigation.ui

import com.artsam.navigation.ui.screens.ItemScreenArgs
import com.artsam.navigation.ui.screens.ItemsScreenProducer
import com.artsam.navigation.ui.screens.ProfileScreenProducer
import com.artsam.navigation.ui.screens.SettingsScreenProducer
import com.artsam.navigation.ui.screens.itemScreenProducer
import com.asamoha.navigation.Route
import kotlinx.parcelize.Parcelize

sealed class AppRoute(
    override val screenProducer: () -> AppScreen,
) : Route {

    @Parcelize
    data class Item(
        val args: ItemScreenArgs
    ) : AppRoute(itemScreenProducer(args))

    sealed class Tab(
        screenProducer: () -> AppScreen,
    ) : AppRoute(screenProducer) {

        @Parcelize
        data object Items : Tab(ItemsScreenProducer)

        @Parcelize
        data object Settings : Tab(SettingsScreenProducer)

        @Parcelize
        data object Profile : Tab(ProfileScreenProducer)
    }
}
