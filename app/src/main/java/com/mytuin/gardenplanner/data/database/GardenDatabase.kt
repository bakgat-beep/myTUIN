package com.mytuin.gardenplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mytuin.gardenplanner.data.entities.GardenEntity

@Database(
    entities = [GardenEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(VocabularyConverters::class)
abstract class GardenDatabase : RoomDatabase()