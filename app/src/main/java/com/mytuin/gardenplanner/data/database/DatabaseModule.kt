package com.mytuin.gardenplanner.data.database

import android.content.Context
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GardenPreferenceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGardenDatabase(
        @ApplicationContext context: Context,
    ): GardenDatabase = GardenDatabaseFactory.create(context)

    @Provides
    fun provideGardenDao(db: GardenDatabase): GardenDao = db.gardenDao()

    @Provides
    fun provideGardenPreferenceDao(db: GardenDatabase): GardenPreferenceDao = db.gardenPreferenceDao()

    @Provides
    fun provideGrowingSpaceDao(db: GardenDatabase): GrowingSpaceDao = db.growingSpaceDao()

    @Provides
    fun provideGrowingSpaceHistoryDao(db: GardenDatabase): GrowingSpaceHistoryDao = db.growingSpaceHistoryDao()

    @Provides
    fun providePlantDao(db: GardenDatabase): PlantDao = db.plantDao()

    @Provides
    fun providePlantAliasDao(db: GardenDatabase): PlantAliasDao = db.plantAliasDao()

    @Provides
    fun provideCultivarDao(db: GardenDatabase): CultivarDao = db.cultivarDao()
}
