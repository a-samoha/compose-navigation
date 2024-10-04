package com.asamoha.navigation

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable

/**
 * Base interface for all navigation routes
 */
@Immutable
interface Route : Parcelable{

    val screenProducer : () -> Screen
}