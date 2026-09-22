package com.mytuin.gardenplanner.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GardenPreferenceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GardenPlantPreferenceEntity
import com.mytuin.gardenplanner.data.entities.GardenPreferenceEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceHistoryEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity

const val GARDEN_DATABASE_VERSION: Int = 6

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
        PlantAliasEntity::class,
        CultivarEntity::class,
    ],
    version = GARDEN_DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
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

    abstract fun plantAliasDao(): PlantAliasDao

    abstract fun cultivarDao(): CultivarDao
}
