package com.artsam.navigation.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artsam.navigation.model.ItemsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditItemViewModel.Factory::class)
class EditItemViewModel @AssistedInject constructor(
    @Assisted private val index: Int, // аргумент, який бцдемо ручками додавати при створенні екземпляру
    private val itemsRepo: ItemsRepository, //аргумент, який Hilt додасть сам (автоматично)
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    init {
        viewModelScope.launch {
            val loadedItem = itemsRepo.getByIndex(index)
            _stateFlow.value = ScreenState.Success(loadedItem)
        }
    }

    fun update(newValue: String) {
        val currentState = _stateFlow.value
        if (currentState !is ScreenState.Success) return
        viewModelScope.launch {
            _stateFlow.value = currentState.copy(isEditInProgress = true)
            itemsRepo.update(index, newValue)
            _exitChannel.send(Unit)
        }
    }

    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(
            val loadedItem: String,
            val isEditInProgress: Boolean = false,
        ) : ScreenState()
    }

    @AssistedFactory
    interface Factory {
        fun create(index: Int): EditItemViewModel
    }
}