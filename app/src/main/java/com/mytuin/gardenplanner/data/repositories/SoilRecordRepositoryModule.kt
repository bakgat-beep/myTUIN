package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SoilRecordRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSoilRecordRepository(impl: SoilRecordRepositoryImpl): SoilRecordRepository
}
