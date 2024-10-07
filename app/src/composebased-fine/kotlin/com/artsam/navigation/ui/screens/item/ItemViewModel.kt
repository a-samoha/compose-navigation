package com.artsam.navigation.ui.screens.item

import androidx.lifecycle.ViewModel
import com.artsam.navigation.ItemsRepository

class ItemViewModel(
    private val args: ItemScreenArgs,
    private val repo: ItemsRepository = ItemsRepository.get()
) : ViewModel() {

    init {
        println("test ItemViewModel-${hashCode()} created")
    }

    fun getInitialValue(): String {
        return when (args) {
            ItemScreenArgs.Add -> ""
            is ItemScreenArgs.Edit -> repo.getItems().value[args.index]
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("test ItemViewModel-${hashCode()} destroyed")
    }
}