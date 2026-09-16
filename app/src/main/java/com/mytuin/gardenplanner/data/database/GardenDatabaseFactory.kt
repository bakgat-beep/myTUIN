package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * The point at which the Room database is constructed.
 *
 * The overload accepting a [name] parameter exists for the vertical
 * slice test (A74), which needs a real file-backed database under an
 * isolated name.
 *
 * V1_DATABASE_SCHEMA.md §67 requires foreign keys to be enforced
 * where practical. SQLite does not enable them by default.
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
            .build()

    val foreignKeysCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = ON")
        }
    }
}