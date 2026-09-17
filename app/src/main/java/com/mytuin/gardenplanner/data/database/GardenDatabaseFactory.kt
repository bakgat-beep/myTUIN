package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mytuin.gardenplanner.data.database.seed.SeedCallback

/**
 * The point at which the Room database is constructed.
 *
 * The overload accepting a [name] parameter exists for tests that
 * need a real file-backed database under an isolated name.
 *
 * Two callbacks are registered:
 *   - foreignKeysCallback: enables SQLite foreign-key enforcement
 *     (D11; V1_DATABASE_SCHEMA §67).
 *   - SeedCallback: applies SeedData on first creation
 *     (§11, §30 item 12).
 *
 * Room accepts multiple callbacks; both run on the appropriate
 * lifecycle event.
 *
 * fallbackToDestructiveMigration is never used.
 */
object GardenDatabaseFactory {

    const val DATABASE_NAME: String = "garden.db"

    fun create(
        context: Context,
        name: String = DATABASE_NAME,
    ): GardenDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            GardenDatabase::class.java,
            name,
        )
            .addCallback(foreignKeysCallback)
            .addCallback(SeedCallback)
            .build()

    val foreignKeysCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = ON")
        }
    }
}