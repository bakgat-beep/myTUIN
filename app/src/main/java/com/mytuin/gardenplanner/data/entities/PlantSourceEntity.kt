package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.SourceRelationship

/**
 * PlantSource.
 *
 * V1_DATABASE_SCHEMA §24. PLANT_VOCABULARIES §41 `sources`.
 * Phase 1 step 4d.
 *
 * Composite primary key (plant_id, source_id).
 */
@Entity(
    tableName = "plant_source",
    primaryKeys = ["plant_id", "source_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = SourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["source_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["source_id"]),
    ],
)
data class PlantSourceEntity(
    val plant_id: String,
    val source_id: String,
    val source_relationship: SourceRelationship? = null,
    val notes: String? = null,
)
