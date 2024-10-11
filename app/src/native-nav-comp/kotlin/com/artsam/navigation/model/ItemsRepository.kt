package com.artsam.navigation.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor(
    // @ApplicationContext private val context: Context,
) {

    private val itemsFlow =
        MutableStateFlow(List(size = 5) { "Item ${it + 1}" })

    suspend fun add(item: String) {
        delay(2000)
        itemsFlow.update { it + item }
    }

    fun getItems(): Flow<List<String>> {
        return itemsFlow.asStateFlow().onStart { delay(3000) }
    }

    suspend fun getByIndex(index: Int): String {
        delay(1000)
        return itemsFlow.value[index]
    }

    suspend fun update(index: Int, newValue: String) {
        delay(2000)
        itemsFlow.update { oldList ->
            oldList.toMutableList().apply { set(index, newValue) }
        }
    }
}