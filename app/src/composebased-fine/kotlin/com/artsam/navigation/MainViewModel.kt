package com.artsam.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ItemsRepository,
) : ViewModel() {

    fun clear() {
        repo.clear()
    }
}