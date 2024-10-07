package com.artsam.navigation.ui.screens.item

import androidx.lifecycle.ViewModel
import com.artsam.navigation.ItemsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ItemViewModel.Factory::class)
class ItemViewModel @AssistedInject constructor(
    @Assisted private val args: ItemScreenArgs,
    private val repo: ItemsRepository,
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

    @AssistedFactory
    interface Factory {
        fun create(args: ItemScreenArgs): ItemViewModel
    }

    override fun onCleared() {
        super.onCleared()
        println("test ItemViewModel-${hashCode()} destroyed")
    }
}