package com.artsam.navigation.ui.screens.items

import androidx.lifecycle.ViewModel
import com.artsam.navigation.ItemsRepository
import com.artsam.navigation.ui.screens.item.ItemScreenArgs
import com.artsam.navigation.ui.screens.item.ItemScreenResponse

class ItemsViewModel(
    private val repo: ItemsRepository = ItemsRepository.get()
) : ViewModel() {

    val itemsFlow = repo.getItems()

    fun processResponse(response: ItemScreenResponse) {
        when (response.args) {
            ItemScreenArgs.Add -> repo.addItem(response.newValue)
            is ItemScreenArgs.Edit -> repo.updateItem(response.args.index, response.newValue)
        }
    }
}