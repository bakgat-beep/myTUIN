package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.PlantRelationshipType

/**
 * PlantRelationship.
 *
 * V1_DATABASE_SCHEMA §22. Phase 1 step 4d.
 *
 * Composite primary key (plant_id, related_plant_id,
 * relationship_type). Both foreign keys reference plant with
 * RESTRICT. A reverse index on related_plant_id supports the
 * inverse lookup.
 *
 * No status column (P41D-4). A wrong relationship is corrected by
 * editing it, not by deprecation.
 */
@Entity(
    tableName = "plant_relationship",
    primaryKeys = ["plant_id", "related_plant_id", "relationship_type"],
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["related_plant_id"],
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
        Index(value = ["related_plant_id"]),
        Index(value = ["source_id"]),
    ],
)
data class PlantRelationshipEntity(
    val plant_id: String,
    val related_plant_id: String,
    val relationship_type: PlantRelationshipType,
    val strength: String? = null,
    val reason: String? = null,
    val source_id: String? = null,
    val confidence: Confidence? = null,
    val notes: String? = null,
)
