package com.mytuin.gardenplanner.data.database.seed

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Applies SeedData at first database creation.
 *
 * A132=a: Room's onCreate callback. Fires exactly once per database
 * file, runs inside the schema-creation transaction, and requires no
 * marker table or preference to guard against double application. If
 * an insert fails, schema creation rolls back with it.
 *
 * A135=a: no OnConflictStrategy.INSERT_OR_IGNORE equivalent here. The
 * raw SQL uses default conflict behaviour (ABORT). If a duplicate
 * primary key is ever attempted, the insert throws loudly rather than
 * silently skipping — the signal that the mechanism ran twice.
 *
 * A138=a: no enable/disable flag. A test that needs an unseeded
 * database can build its own RoomDatabase without this callback.
 */
object SeedCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        SeedData.insertStatements.forEach { db.execSQL(it) }
    }
}