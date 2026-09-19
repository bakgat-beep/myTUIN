package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GrowingSpaceRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGrowingSpaceRepository(impl: GrowingSpaceRepositoryImpl): GrowingSpaceRepository
}
