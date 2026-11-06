package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.SpatialObjectHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpatialObjectHistoryRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSpatialObjectHistoryRepository(impl: SpatialObjectHistoryRepositoryImpl): SpatialObjectHistoryRepository
}
