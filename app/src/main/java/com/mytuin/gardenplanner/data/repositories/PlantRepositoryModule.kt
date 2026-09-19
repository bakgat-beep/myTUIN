package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.PlantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds PlantRepository to its Room-backed implementation.
 * Colocated with the implementation (A29=C).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PlantRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPlantRepository(impl: PlantRepositoryImpl): PlantRepository
}
