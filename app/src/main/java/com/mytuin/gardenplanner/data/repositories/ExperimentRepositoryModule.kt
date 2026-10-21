package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.ExperimentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExperimentRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExperimentRepository(impl: ExperimentRepositoryImpl): ExperimentRepository
}
