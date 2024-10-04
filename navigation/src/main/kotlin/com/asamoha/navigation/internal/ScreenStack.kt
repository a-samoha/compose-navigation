package com.asamoha.navigation.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.os.ParcelCompat
import com.asamoha.navigation.NavigationState
import com.asamoha.navigation.Route
import com.asamoha.navigation.Router
import com.asamoha.navigation.Screen
import com.asamoha.navigation.ScreenResponseReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Core navigation stack implementation.
 */
internal class ScreenStack(
    private val routes: SnapshotStateList<RouteRecord>,
    private val screenResponsesBus: ScreenResponsesBus = ScreenResponsesBus(),
) : NavigationState, InternalNavigationState, Router, Parcelable {

    constructor(parcel: Parcel) : this(
        SnapshotStateList<RouteRecord>().also { newList ->
            ParcelCompat.readList(
                parcel,
                newList,
                RouteRecord::class.java.classLoader,
                RouteRecord::class.java,
            )
        }
    )

    // region NavigationState impl
    override val isRoot: Boolean
        get() = routes.size == 1

    override val currentRoute: Route
        get() = routes.last().route

    override val currentScreen: Screen
            by derivedStateOf { currentRoute.screenProducer() }
    // endregion

    // region InternalNavigationState impl
    override val currentUuid: String
        get() = routes.last().uuid

    override val screenResponseReceiver: ScreenResponseReceiver = screenResponsesBus

    private val eventsFlow = MutableSharedFlow<NavigationEvent>(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    override fun listen(): Flow<NavigationEvent> {
        return eventsFlow
    }
    // endregion

    // region Router impl
    override fun launch(route: Route) {
        screenResponsesBus.cleanUp()
        routes.add(RouteRecord(route))
    }

    override fun pop(response: Any?) {
        val removedRouteRecord = routes.removeLastOrNull()
        if (removedRouteRecord != null) {
            eventsFlow.tryEmit(NavigationEvent.Removed(removedRouteRecord))
            if (response != null) {
                screenResponsesBus.send(response)
            }
        }
    }

    override fun restart(route: Route) {
        screenResponsesBus.cleanUp()
        routes.apply {
            routes.forEach {
                eventsFlow.tryEmit(NavigationEvent.Removed(it))
            }
            clear()
            add(RouteRecord(route))
        }
    }
    // endregion

    // region Parcelable impl
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(routes)
    }

    companion object CREATOR : Parcelable.Creator<ScreenStack> {
        override fun createFromParcel(parcel: Parcel): ScreenStack {
            return ScreenStack(parcel)
        }

        override fun newArray(size: Int): Array<ScreenStack?> {
            return arrayOfNulls(size)
        }
    }
    // endregion

}