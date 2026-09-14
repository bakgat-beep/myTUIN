package com.mytuin.gardenplanner.data.database

import androidx.room.TypeConverter
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Room TypeConverters for canonical vocabulary enums.
 *
 * DEC-040: stored values use the canonical identifier; the enum
 * constant name is never written to the database. Reading an unknown
 * id from the database is a validation error, not a silent fallback.
 */
class VocabularyConverters {

    @TypeConverter
    fun hemisphereToId(value: Hemisphere): String = value.id

    @TypeConverter
    fun idToHemisphere(id: String): Hemisphere =
        Hemisphere.fromId(id)
            ?: error("Unknown Hemisphere id stored in database: '$id'")

    @TypeConverter
    fun recordStatusToId(value: RecordStatus): String = value.id

    @TypeConverter
    fun idToRecordStatus(id: String): RecordStatus =
        RecordStatus.fromId(id)
            ?: error("Unknown RecordStatus id stored in database: '$id'")

    @TypeConverter
    fun plantLifecycleToId(value: PlantLifecycle): String = value.id

    @TypeConverter
    fun idToPlantLifecycle(id: String): PlantLifecycle =
        PlantLifecycle.fromId(id)
            ?: error("Unknown PlantLifecycle id stored in database: '$id'")

    @TypeConverter
    fun plantAliasTypeToId(value: PlantAliasType): String = value.id

    @TypeConverter
    fun idToPlantAliasType(id: String): PlantAliasType =
        PlantAliasType.fromId(id)
            ?: error("Unknown PlantAliasType id stored in database: '$id'")
}