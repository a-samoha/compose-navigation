package com.asamoha.navigation.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * [ScreenViewModelStoreOwner] реалізує нативний інтерфейс ViewModelStoreOwner
 *
 * Activity/Fragment реалізують даний інтерфейс тому підтрисують ViewModel "з коробки"
 */
internal class ScreenViewModelStoreOwner(
    screenViewModelStoreProvider: ScreenViewModelStoreProvider,
    uuid: String,
) : ViewModelStoreOwner {

    override val viewModelStore: ViewModelStore =
        screenViewModelStoreProvider.getStore(uuid)
}