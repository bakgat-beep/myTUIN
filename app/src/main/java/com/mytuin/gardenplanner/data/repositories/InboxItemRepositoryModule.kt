package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InboxItemRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindInboxItemRepository(impl: InboxItemRepositoryImpl): InboxItemRepository
}
