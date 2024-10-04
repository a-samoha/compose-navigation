package com.asamoha.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable

/**
 * Base interface for all navigation routes
 */
@Immutable
interface Route : Parcelable {

    /**
     * Create an instance of the [Screen] represented by this route.
     * Please note that the screen can have shorter lifecycle than the route itself.
     * For example ahen You launch another screen above the current,
     * the latter one will be destroyed but the route itself keeps alive.
     * Then, when the user goes back, a new screen instance is created again.
     */
    val screenProducer: () -> Screen
}