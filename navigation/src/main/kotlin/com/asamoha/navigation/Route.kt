package com.asamoha.navigation

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable

/**
 * Base interface for all navigation routes
 */
@Immutable
interface Route : Parcelable

object EmptyRoute : Route {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(0)
    }
}