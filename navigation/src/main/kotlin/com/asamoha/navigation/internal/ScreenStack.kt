package com.asamoha.navigation.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.core.os.ParcelCompat
import com.asamoha.navigation.Route
import com.asamoha.navigation.Screen
import com.asamoha.navigation.ScreenResponseReceiver

/**
 * Core navigation stack implementation.
 */
internal class ScreenStack(
    private val routes: SnapshotStateList<RouteRecord>,
    private val screenResponsesBus: ScreenResponsesBus = ScreenResponsesBus(),
) : Parcelable {

    constructor(routes:List<Route>) : this(
        routes.map(::RouteRecord).toMutableStateList()
    )

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

    constructor(rootRoute: Route) : this(
        routes = mutableStateListOf(RouteRecord(rootRoute))
    )

    // region NavigationState impl
    val isRoot: Boolean
        get() = routes.size == 1

    val currentRoute: Route
        get() = routes.last().route

    val currentScreen: Screen
            by derivedStateOf { currentRoute.screenProducer() }
    // endregion

    // region InternalNavigationState impl
    val currentUuid: String
        get() = routes.last().uuid

    val screenResponseReceiver: ScreenResponseReceiver = screenResponsesBus

    fun getAllRouteRecords(): List<RouteRecord> = routes
    // endregion

    // region Router impl
    fun launch(route: Route) {
        screenResponsesBus.cleanUp()
        routes.add(RouteRecord(route))
    }

    fun pop(response: Any?): RouteRecord? {
        val removedRouteRecord = routes.removeLastOrNull()
        if (removedRouteRecord != null) {
            if (response != null) {
                screenResponsesBus.send(response)
            }
        }
        return removedRouteRecord
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