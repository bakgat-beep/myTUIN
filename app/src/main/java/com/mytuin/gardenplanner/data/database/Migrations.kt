package com.mytuin.gardenplanner.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Manual migrations.
 *
 * Until this file existed, every schema change was an
 * @AutoMigration. MIGRATION_19_20 is the first non-auto migration:
 * it changes the stored value of vocabulary ids without changing
 * the SQL schema. Room's AutoMigration machinery cannot express
 * data-only updates, so a Migration class is required.
 *
 * Register at runtime via Room's addMigrations in
 * GardenDatabaseFactory.
 */
object Migrations {
    /**
     * Step 4c-A: unit vocabulary unification (DEC-046).
     *
     * ActivityQuantityUnit already used singular ids; MeasurementUnit
     * used plural for hour and minute. QuantityUnit uses singular.
     * This migration rewrites any existing measurement rows that
     * stored the plural forms.
     *
     * No schema change: the unit column remains TEXT on both tables.
     * Only the set of permitted id values changes.
     */
    val MIGRATION_19_20: Migration =
        object : Migration(19, 20) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("UPDATE measurement SET unit = 'hour' WHERE unit = 'hours'")
                db.execSQL("UPDATE measurement SET unit = 'minute' WHERE unit = 'minutes'")
            }
        }
}
