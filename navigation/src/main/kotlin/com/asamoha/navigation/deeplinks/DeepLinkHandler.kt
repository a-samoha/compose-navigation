package com.asamoha.navigation.deeplinks

import android.net.Uri

fun interface DeepLinkHandler {

    fun handleDeeplink(
        uri: Uri, // Universal resource identifier (Унікальний ідентифікатор)
        inputState: MultistackState,
    ): MultistackState

    companion object {
        val DEFAULT = DeepLinkHandler { uri, inputstate -> inputstate }
    }
}