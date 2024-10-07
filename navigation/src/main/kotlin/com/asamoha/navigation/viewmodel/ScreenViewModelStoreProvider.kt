package com.asamoha.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/**
 * Наш кастомний [ScreenViewModelStoreProvider] наслідує нативну ViewModel
 * щоб переживати поворот екрану і всі ViewModel будуть зберігатись
 */
internal class ScreenViewModelStoreProvider : ViewModel() {

    /**
     * [ViewModelStore] це колекція активних ViewModel для екрана
     * якщо необхідна ViewModel вже існує у ViewModelStore - вона не буде створюватись з нуля.
     * [stores] це мапа де кожен екран (за ідентифікатором String) має власний ViewModelStore
     */
    private val stores = mutableMapOf<String, ViewModelStore>()

    fun removeStore(uuid: String) {
        val store = stores.remove(uuid)
        store?.clear()
    }

    fun getStore(uuid: String): ViewModelStore {
        return stores.computeIfAbsent(uuid) { ViewModelStore() }
    }

    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}