package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * The point at which the Room database is constructed.
 *
 * Not wired into Hilt yet — no production code calls create() in step 4.
 * Step 5 (repositories and DI wiring) makes the first call.
 *
 * V1_DATABASE_SCHEMA.md §67 requires foreign keys to be enforced where
 * practical. SQLite does not enable them by default.
 */
object GardenDatabaseFactory {

    const val DATABASE_NAME: String = "garden.db"

    fun create(context: Context): GardenDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            GardenDatabase::class.java,
            DATABASE_NAME,
        )
            .addCallback(foreignKeysCallback)
            .build()

    /**
     * Exposed so tests can install the same pragma behaviour on
     * in-memory databases. Without this, foreign-key enforcement is
     * off in tests, and FK-violation tests would pass for the wrong
     * reason.
     */
    val foreignKeysCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = ON")
        }
    }
}