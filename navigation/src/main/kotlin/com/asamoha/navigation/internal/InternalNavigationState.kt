package com.asamoha.navigation.internal

import kotlinx.coroutines.flow.Flow

internal sealed class NavigationEvent {

    data class Removed(val routeRecord: RouteRecord) : NavigationEvent()
}

internal interface InternalNavigationState {

    val currentUuid: String

    fun listen(): Flow<NavigationEvent>
}