package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

@Entity(tableName = "garden")
data class GardenEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val created_at: Long,
    val updated_at: Long,
    val status: RecordStatus,
    val description: String? = null,
    val country_code: String? = null,
    val region: String? = null,
    val locality: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezone: String? = null,
    val hemisphere: Hemisphere = Hemisphere.UNKNOWN,
)
