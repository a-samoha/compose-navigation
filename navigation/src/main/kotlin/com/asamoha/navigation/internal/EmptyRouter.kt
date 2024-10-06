package com.asamoha.navigation.internal

import com.asamoha.navigation.Route
import com.asamoha.navigation.Router

internal class EmptyRouter : Router {
    override fun launch(route: Route) = Unit

    override fun pop(response: Any?) = Unit

    override fun restart(rootRoutes: List<Route>, initialIndex: Int) = Unit

    override fun switchStack(index: Int) = Unit
}