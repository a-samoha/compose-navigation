package com.asamoha.navigation.deeplinks

import com.asamoha.navigation.Route

data class MultistackState(
    val stacks: List<StackState>,
    val activeStackIndex: Int,
) {
    fun withNewRoute(
        stackIndex: Int,
        route: Route,
    ): MultistackState {
        val modifiedStacks = stacks.toMutableList()
        modifiedStacks[stackIndex] = modifiedStacks[stackIndex].withNewRoute(route)
        return  copy(
            activeStackIndex = stackIndex,
            stacks = modifiedStacks,
        )
    }
}