package com.asamoha.navigation.deeplinks

import android.net.Uri

fun interface DeepLinkHandler {

    fun handleDeeplink(
        // Uri - Universal resource identifier (Унікальний ідентифікатор) напр.: "nav://settings"
        // URL - Uniform Resource Locator унікальна сукупність символів, що відображає шлях до сторінки інтернет-ресурсу, напр.: "https://goit.global"
        uri: Uri,
        inputState: MultistackState,
    ): MultistackState

    companion object {
        val DEFAULT = DeepLinkHandler { uri, inputstate -> inputstate }
    }
}