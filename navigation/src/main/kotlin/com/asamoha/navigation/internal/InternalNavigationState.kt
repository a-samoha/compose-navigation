package com.asamoha.navigation.internal

import com.asamoha.navigation.ScreenResponseReceiver
import kotlinx.coroutines.flow.Flow

internal sealed class NavigationEvent {

    data class Removed(val routeRecord: RouteRecord) : NavigationEvent()
}

internal interface InternalNavigationState {

    val currentUuid: String

    val screenResponseReceiver: ScreenResponseReceiver

    fun listen(): Flow<NavigationEvent>
}