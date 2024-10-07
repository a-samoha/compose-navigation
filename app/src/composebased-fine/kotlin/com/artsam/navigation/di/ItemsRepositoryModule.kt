package com.artsam.navigation.di

import com.artsam.navigation.ItemsRepository
import com.artsam.navigation.ItemsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ItemsRepositoryModule {

    @Binds
    fun itemsRepository(impl: ItemsRepositoryImpl): ItemsRepository
}