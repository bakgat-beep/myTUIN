package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpatialObjectRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSpatialObjectRepository(impl: SpatialObjectRepositoryImpl): SpatialObjectRepository
}
