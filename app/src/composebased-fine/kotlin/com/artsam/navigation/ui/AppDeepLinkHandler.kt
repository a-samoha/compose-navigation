package com.artsam.navigation.ui

import android.net.Uri
import com.artsam.navigation.ui.screens.ItemScreenArgs
import com.asamoha.navigation.deeplinks.DeepLinkHandler
import com.asamoha.navigation.deeplinks.MultistackState

object AppDeepLinkHandler : DeepLinkHandler {

    override fun handleDeeplink(uri: Uri, inputState: MultistackState): MultistackState {
        var outputState = inputState
        if (uri.scheme == "nav") {
            if (uri.host == "settings") {
                outputState = inputState.copy(activeStackIndex = 1)
            } else if (uri.host == "items") {
                val itemIndex = uri.pathSegments?.firstOrNull()?.toIntOrNull()
                if (itemIndex != null) {
                    val editItemRoute = AppRoute.Item(ItemScreenArgs.Edit(itemIndex))
                    outputState = inputState.withNewRoute(stackIndex = 0, editItemRoute)
                }
            }
        }
        return outputState
    }
}