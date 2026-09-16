package com.mytuin.gardenplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.dao.GardenDao

/**
 * The V1 Room database.
 *
 * PHASE_0_PROJECT_FOUNDATION §10, §30 items 6–9.
 * V1_DATABASE_SCHEMA.md §7: schema_version = 1.
 *
 * Schema version history:
 *   v1 — Garden (step 3); Plant, PlantAlias, Cultivar (step 4).
 *
 * The schema version remains 1 (A13=C) because the application has
 * never run on a device with real user data. Step 3's committed
 * 1.json is overwritten. Any test device that ran the step 3 build
 * must have its app data cleared once, because Room compares
 * identity hashes on open.
 *
 * fallbackToDestructiveMigration is never used. PHASE_0_PROJECT_FOUNDATION
 * §31; DATA_MIGRATION_STRATEGY §52.
 */
@Database(
    entities = [
        GardenEntity::class,
        PlantEntity::class,
        PlantAliasEntity::class,
        CultivarEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(VocabularyConverters::class)
abstract class GardenDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao
    abstract fun plantAliasDao(): PlantAliasDao
    abstract fun cultivarDao(): CultivarDao

    abstract fun gardenDao(): GardenDao
}