package com.asamoha.navigation.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.os.ParcelCompat
import com.asamoha.navigation.NavigationState
import com.asamoha.navigation.Route
import com.asamoha.navigation.Router
import com.asamoha.navigation.Screen
import com.asamoha.navigation.ScreenResponseReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class ScreenMultiStack(
    private val stacks: SnapshotStateList<ScreenStack>,
    initialIndex: Int,
) : NavigationState, InternalNavigationState, Router, Parcelable {

    private val currentStack: ScreenStack get() = stacks[currentStackIndex]

    // region NavigationState
    override val isRoot: Boolean get() = currentStack.isRoot
    override val currentRoute: Route get() = currentStack.currentRoute
    override val currentScreen: Screen get() = currentStack.currentScreen
    override var currentStackIndex: Int by mutableStateOf(initialIndex)
    // endregion

    // region InternalNavigationState impl
    override val currentUuid: String get() = currentStack.currentUuid
    override val screenResponseReceiver: ScreenResponseReceiver get() = currentStack.screenResponseReceiver

    private val eventsFlow = MutableSharedFlow<NavigationEvent>(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    override fun listen(): Flow<NavigationEvent> {
        return eventsFlow
    }
    // endregion

    // region Router impl

    override fun launch(route: Route) {
        currentStack.launch(route)
    }

    override fun pop(response: Any?) {
        val removedRouteRecord = currentStack.pop(response)
        if (removedRouteRecord != null) {
            eventsFlow.tryEmit(NavigationEvent.Removed(removedRouteRecord))
        }
    }

    override fun restart(rootRoutes: List<Route>, initialIndex: Int) {
        stacks.flatMap { it.getAllRouteRecords() }
            .map { NavigationEvent.Removed(it) }
            .forEach(eventsFlow::tryEmit)
        stacks.clear()
        stacks.addAll(rootRoutes.map(::ScreenStack))
        switchStack(initialIndex) // todo check if initialIndex is in list bounds (check fo IndexOutOfBoundsException)
    }

    override fun switchStack(index: Int) {
        currentStackIndex = index
    }
    // endregion

    // region Parcelable impl
    constructor(parcel: Parcel) : this(
        stacks = SnapshotStateList<ScreenStack>().also { newList ->
            ParcelCompat.readList(
                parcel,
                newList,
                ScreenStack::class.java.classLoader,
                ScreenStack::class.java,
            )
        },
        initialIndex = parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(stacks)
        parcel.writeInt(currentStackIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScreenMultiStack> {
        override fun createFromParcel(parcel: Parcel): ScreenMultiStack {
            return ScreenMultiStack(parcel)
        }

        override fun newArray(size: Int): Array<ScreenMultiStack?> {
            return arrayOfNulls(size)
        }
    }
    // endregion
}