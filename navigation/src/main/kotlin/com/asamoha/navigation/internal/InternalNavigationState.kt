package com.asamoha.navigation.internal

import com.asamoha.navigation.Route
import kotlinx.coroutines.flow.Flow

internal sealed class NavigationEvent {

    data class Removed(val routeRecord: RouteRecord) : NavigationEvent()
}

internal interface InternalNavigationState {

    fun listen(): Flow<NavigationEvent>
}