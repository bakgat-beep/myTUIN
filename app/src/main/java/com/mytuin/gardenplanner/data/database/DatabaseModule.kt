package com.mytuin.gardenplanner.data.database

import android.content.Context
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the Room database and its DAOs.
 *
 * Colocated with what it provides (A29=C; CONTRIBUTION_AND_DEVELOPMENT_
 * GUIDELINES §9).
 *
 * GardenDatabaseFactory.create() carries the foreign_keys pragma
 * (step 3, D11). Routing the production path through the factory is
 * the first time that pragma runs on a real device (A30=A).
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGardenDatabase(
        @ApplicationContext context: Context,
    ): GardenDatabase = GardenDatabaseFactory.create(context)

    @Provides
    fun providePlantDao(db: GardenDatabase): PlantDao = db.plantDao()

    @Provides
    fun providePlantAliasDao(db: GardenDatabase): PlantAliasDao = db.plantAliasDao()

    @Provides
    fun provideCultivarDao(db: GardenDatabase): CultivarDao = db.cultivarDao()
}