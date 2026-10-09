package com.mytuin.gardenplanner.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import com.mytuin.gardenplanner.data.dao.PlanDao
import com.mytuin.gardenplanner.data.dao.PlanTargetDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceHistoryDao
import com.mytuin.gardenplanner.data.dao.ProblemDao
import com.mytuin.gardenplanner.data.dao.ProblemObservationDao
import com.mytuin.gardenplanner.data.dao.SourceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GardenPlantPreferenceEntity
import com.mytuin.gardenplanner.data.entities.GardenPreferenceEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceHistoryEntity
import com.mytuin.gardenplanner.data.entities.HarvestEntity
import com.mytuin.gardenplanner.data.entities.HarvestLossEntity
import com.mytuin.gardenplanner.data.entities.MeasurementEntity
import com.mytuin.gardenplanner.data.entities.ObservationEntity
import com.mytuin.gardenplanner.data.entities.PlanEntity
import com.mytuin.gardenplanner.data.entities.PlanTargetEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceHistoryEntity
import com.mytuin.gardenplanner.data.entities.ProblemEntity
import com.mytuin.gardenplanner.data.entities.ProblemObservationEntity
import com.mytuin.gardenplanner.data.entities.SourceEntity
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity

const val GARDEN_DATABASE_VERSION: Int = 17

/**
 * The V1 Room database.
 *
 * Schema version history:
 *   v1 — Garden, Plant, PlantAlias, Cultivar.
 *   v2 — adds GrowingSpace (step 5c).
 *   v3 — adds GrowingSpaceHistory (step 5d, DEC-041).
 *   v4 — adds GardenPreference and GardenPlantPreference (step 6i,
 *        DEC-042).
 *   v5 — adds Area (Phase 1 step 1a).
 *   v6 — adds SpatialObject (Phase 1 step 1b).
 *   v7 — adds area_id to growing_space (Phase 1 step 1c).
 *   v8 — adds PlantInstance (Phase 1 step 2a).
 *   v9 — adds PlantInstanceHistory (Phase 1 step 2b, DEC-041).
 *   v10 — adds Activity (Phase 1 step 3a).
 *   v11 — adds Observation (Phase 1 step 3b, DEC-045).
 *   v12 — adds Measurement (Phase 1 step 3c).
 *   v13 — adds Harvest (Phase 1 step 3d).
 *   v14 — adds HarvestLoss (Phase 1 step 3d').
 *   v15 — adds Problem and ProblemObservation (Phase 1 step 3e).
 *   v16 — adds Plan and PlanTarget; adds plan_id to activity (Phase 1 step 3f).
 *   v17 — adds Source (Phase 1 step 3g).
 *
 * All migrations are @AutoMigration. Room derives the SQL from the
 * schema diff at compile time.
 *
 * fallbackToDestructiveMigration is never used.
 */
@Database(
    entities = [
        GardenEntity::class,
        GardenPreferenceEntity::class,
        GardenPlantPreferenceEntity::class,
        AreaEntity::class,
        SpatialObjectEntity::class,
        GrowingSpaceEntity::class,
        GrowingSpaceHistoryEntity::class,
        PlantEntity::class,
        PlantInstanceEntity::class,
        PlantInstanceHistoryEntity::class,
        PlantAliasEntity::class,
        CultivarEntity::class,
        ActivityEntity::class,
        ObservationEntity::class,
        MeasurementEntity::class,
        HarvestEntity::class,
        HarvestLossEntity::class,
        ProblemEntity::class,
        ProblemObservationEntity::class,
        PlanEntity::class,
        PlanTargetEntity::class,
        SourceEntity::class,
    ],
    version = GARDEN_DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
        AutoMigration(from = 13, to = 14),
        AutoMigration(from = 14, to = 15),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17),
    ],
)
@TypeConverters(VocabularyConverters::class)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao

    abstract fun gardenPreferenceDao(): GardenPreferenceDao

    abstract fun areaDao(): AreaDao

    abstract fun spatialObjectDao(): SpatialObjectDao

    abstract fun growingSpaceDao(): GrowingSpaceDao

    abstract fun growingSpaceHistoryDao(): GrowingSpaceHistoryDao

    abstract fun plantDao(): PlantDao

    abstract fun plantInstanceDao(): PlantInstanceDao

    abstract fun plantInstanceHistoryDao(): PlantInstanceHistoryDao

    abstract fun plantAliasDao(): PlantAliasDao

    abstract fun cultivarDao(): CultivarDao

    abstract fun activityDao(): ActivityDao

    abstract fun observationDao(): ObservationDao

    abstract fun measurementDao(): MeasurementDao

    abstract fun harvestDao(): HarvestDao

    abstract fun harvestLossDao(): HarvestLossDao

    abstract fun problemDao(): ProblemDao

    abstract fun problemObservationDao(): ProblemObservationDao

    abstract fun planDao(): PlanDao

    abstract fun planTargetDao(): PlanTargetDao

    abstract fun sourceDao(): SourceDao
}
