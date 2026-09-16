package com.mytuin.gardenplanner.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceHistoryEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity

/**
 * The current database schema version.
 *
 * Single source of truth (A79=a). Referenced from the @Database
 * annotation below, and from the diagnostic footer. The two cannot
 * drift.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 5, §31 (Project).
 * RELEASE_AND_VERSIONING §29: application version and schema version
 * are related but separate.
 */
const val GARDEN_DATABASE_VERSION: Int = 3

/**
 * The V1 Room database.
 *
 * Schema version history:
 *   v1 — Garden, Plant, PlantAlias, Cultivar.
 *   v2 — adds GrowingSpace (step 5c).
 *   v3 — adds GrowingSpaceHistory (step 5d, DEC-041).
 *
 * Both migrations are @AutoMigration. Room derives the SQL from the
 * schema diff at compile time.
 *
 * fallbackToDestructiveMigration is never used.
 */
@Database(
    entities = [
        GardenEntity::class,
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
    ],
)
@TypeConverters(VocabularyConverters::class)
abstract class GardenDatabase : RoomDatabase() {

    abstract fun gardenDao(): GardenDao
    abstract fun growingSpaceDao(): GrowingSpaceDao
    abstract fun growingSpaceHistoryDao(): GrowingSpaceHistoryDao
    abstract fun plantDao(): PlantDao
    abstract fun plantAliasDao(): PlantAliasDao
    abstract fun cultivarDao(): CultivarDao
}