package com.asamoha.navigation.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.asamoha.navigation.EmptyRoute
import com.asamoha.navigation.Route
import java.util.UUID

@Immutable
internal data class RouteRecord(
    val uuid: String,
    val route: Route,
) : Parcelable {

    constructor(route: Route) : this(
        uuid = UUID.randomUUID().toString(),
        route = route,
    )

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readParcelable(Route::class.java.classLoader, Route::class.java) ?: EmptyRoute
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeParcelable(route, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RouteRecord> {
        override fun createFromParcel(parcel: Parcel): RouteRecord {
            return RouteRecord(parcel)
        }

        override fun newArray(size: Int): Array<RouteRecord?> {
            return arrayOfNulls(size)
        }
    }
}
