package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.GardenPreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GardenPreferenceRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGardenPreferenceRepository(impl: GardenPreferenceRepositoryImpl): GardenPreferenceRepository
}
