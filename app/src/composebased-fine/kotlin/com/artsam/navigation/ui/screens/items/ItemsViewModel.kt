package com.artsam.navigation.ui.screens.items

import androidx.lifecycle.ViewModel
import com.artsam.navigation.ItemsRepository
import com.artsam.navigation.ui.screens.item.ItemScreenArgs
import com.artsam.navigation.ui.screens.item.ItemScreenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val repo: ItemsRepository,
) : ViewModel() {

    val itemsFlow = repo.getItems()

    fun processResponse(response: ItemScreenResponse) {
        when (response.args) {
            ItemScreenArgs.Add -> repo.addItem(response.newValue)
            is ItemScreenArgs.Edit -> repo.updateItem(response.args.index, response.newValue)
        }
    }
}