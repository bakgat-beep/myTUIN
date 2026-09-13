package com.mytuin.gardenplanner.data.database

import androidx.room.TypeConverter
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

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
}