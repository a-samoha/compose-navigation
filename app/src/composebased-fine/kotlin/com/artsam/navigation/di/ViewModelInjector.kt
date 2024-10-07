package com.artsam.navigation.di

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import dagger.hilt.android.lifecycle.addCreationCallback

@Composable
inline fun <reified T : ViewModel> injectViewModel(): T {
    return injectViewModel<T, Any>(creationCallback = null)
}

@Composable
inline fun <reified T : ViewModel, F> injectViewModel(
    noinline creationCallback: ((F) -> T)?,
): T {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
        ?: throw IllegalStateException("injectViewModel() can't find ViewModelStoreOwner")
    val activity = LocalContext.current as? Activity
        ?: throw IllegalStateException("injectViewModel() can't find an Activity")
    val hiltViewModelFactory = HiltViewModelFactory.createInternal(
        activity,
        ViewModelProvider.NewInstanceFactory()
    )
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = hiltViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(SAVED_STATE_REGISTRY_OWNER_KEY, LocalSavedStateRegistryOwner.current)
            set(VIEW_MODEL_STORE_OWNER_KEY, viewModelStoreOwner)
            if (creationCallback != null)
                addCreationCallback(creationCallback)
        }
    )
}