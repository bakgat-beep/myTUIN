package com.mytuin.gardenplanner.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity

/**
 * The V1 Room database.
 *
 * Schema version history:
 *   v1 — Garden, Plant, PlantAlias, Cultivar.
 *   v2 — adds GrowingSpace (step 5c, A60=B).
 *
 * Migration 1→2 is an @AutoMigration (A60b=a). Room derives the SQL
 * from the schema diff at compile time, so the migration cannot drift
 * from the entity definition. The migration test fixtures in
 * GardenDatabaseMigrationTest exercise it end-to-end.
 *
 * The v1 schema JSON is preserved (DATA_MIGRATION_STRATEGY §119).
 *
 * fallbackToDestructiveMigration is never used. PHASE_0_PROJECT_FOUNDATION
 * §31; DATA_MIGRATION_STRATEGY §52.
 */
@Database(
    entities = [
        GardenEntity::class,
        GrowingSpaceEntity::class,
        PlantEntity::class,
        PlantAliasEntity::class,
        CultivarEntity::class,
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@TypeConverters(VocabularyConverters::class)
abstract class GardenDatabase : RoomDatabase() {

    abstract fun gardenDao(): GardenDao
    abstract fun growingSpaceDao(): GrowingSpaceDao
    abstract fun plantDao(): PlantDao
    abstract fun plantAliasDao(): PlantAliasDao
    abstract fun cultivarDao(): CultivarDao
}