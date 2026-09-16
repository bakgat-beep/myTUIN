package com.mytuin.gardenplanner.platform.di

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
import com.mytuin.gardenplanner.platform.time.SystemClock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds platform-service interfaces to their implementations.
 *
 * Colocated with the implementations it provides (A29=C).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PlatformModule {

    @Binds
    @Singleton
    abstract fun bindIdGenerator(impl: UuidIdGenerator): IdGenerator

    @Binds
    @Singleton
    abstract fun bindClock(impl: SystemClock): Clock
}