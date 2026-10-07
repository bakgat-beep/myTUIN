package com.mytuin.gardenplanner.data.database

import android.content.Context
import com.mytuin.gardenplanner.data.dao.ActivityDao
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GardenPreferenceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.data.dao.HarvestDao
import com.mytuin.gardenplanner.data.dao.HarvestLossDao
import com.mytuin.gardenplanner.data.dao.MeasurementDao
import com.mytuin.gardenplanner.data.dao.ObservationDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceHistoryDao
import com.mytuin.gardenplanner.data.dao.ProblemDao
import com.mytuin.gardenplanner.data.dao.ProblemObservationDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
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
    fun provideAreaDao(db: GardenDatabase): AreaDao = db.areaDao()

    @Provides
    fun provideSpatialObjectDao(db: GardenDatabase): SpatialObjectDao = db.spatialObjectDao()

    @Provides
    fun provideGrowingSpaceDao(db: GardenDatabase): GrowingSpaceDao = db.growingSpaceDao()

    @Provides
    fun provideGrowingSpaceHistoryDao(db: GardenDatabase): GrowingSpaceHistoryDao = db.growingSpaceHistoryDao()

    @Provides
    fun providePlantDao(db: GardenDatabase): PlantDao = db.plantDao()

    @Provides
    fun providePlantInstanceDao(db: GardenDatabase): PlantInstanceDao = db.plantInstanceDao()

    @Provides
    fun providePlantInstanceHistoryDao(db: GardenDatabase): PlantInstanceHistoryDao = db.plantInstanceHistoryDao()

    @Provides
    fun providePlantAliasDao(db: GardenDatabase): PlantAliasDao = db.plantAliasDao()

    @Provides
    fun provideCultivarDao(db: GardenDatabase): CultivarDao = db.cultivarDao()

    @Provides
    fun provideActivityDao(db: GardenDatabase): ActivityDao = db.activityDao()

    @Provides
    fun provideObservationDao(db: GardenDatabase): ObservationDao = db.observationDao()

    @Provides
    fun provideMeasurementDao(db: GardenDatabase): MeasurementDao = db.measurementDao()

    @Provides
    fun provideHarvestDao(db: GardenDatabase): HarvestDao = db.harvestDao()

    @Provides
    fun provideHarvestLossDao(db: GardenDatabase): HarvestLossDao = db.harvestLossDao()

    @Provides
    fun provideProblemDao(db: GardenDatabase): ProblemDao = db.problemDao()

    @Provides
    fun provideProblemObservationDao(db: GardenDatabase): ProblemObservationDao = db.problemObservationDao()
}
