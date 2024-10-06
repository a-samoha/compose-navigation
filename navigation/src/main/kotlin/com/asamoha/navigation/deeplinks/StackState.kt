package com.asamoha.navigation.deeplinks

import com.asamoha.navigation.Route

data class StackState(
    val routes: List<Route>
) {

    fun withNewRoute(route: Route): StackState = copy(
        routes = routes + route
    )
}
