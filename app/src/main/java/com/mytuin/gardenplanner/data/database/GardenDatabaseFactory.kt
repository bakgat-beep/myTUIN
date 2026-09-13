package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object GardenDatabaseFactory {

    const val DATABASE_NAME: String = "garden.db"

    fun create(context: Context): GardenDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            GardenDatabase::class.java,
            DATABASE_NAME,
        )
            .addCallback(ForeignKeysCallback)
            .build()

    private object ForeignKeysCallback : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = ON")
        }
    }
}