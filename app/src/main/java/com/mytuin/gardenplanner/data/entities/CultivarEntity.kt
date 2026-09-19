package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Cultivar.
 *
 * V1_DATABASE_SCHEMA.md §18. DATA_MODEL.md §14.
 *
 * A Cultivar belongs to exactly one Plant. Its identifier is
 * independent of the Plant's identity; renaming either does not
 * change identity.
 *
 * Stable identifier format: "cultivar_<uuid>" (D1).
 *
 * Foreign key to Plant is RESTRICT (A6). Index on plant_id is
 * required for the FK lookup path and for "list cultivars of this
 * plant" queries (A15; V1_DATABASE_SCHEMA §68).
 *
 * Cultivar-specific knowledge (maturity, frost sensitivity, etc.) is
 * intentionally not modelled here. PLANT_VOCABULARIES §26 requires
 * cultivar-specific values to be stored at cultivar level rather
 * than copied into the general plant record, but the mechanism for
 * that storage belongs to a later knowledge step, not to this entity.
 */
@Entity(
    tableName = "cultivar",
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["plant_id"]),
    ],
)
data class CultivarEntity(
    @PrimaryKey
    val id: String,
    val plant_id: String,
    val name: String,
    val created_at: Long,
    val updated_at: Long,
    val status: RecordStatus,
    val description: String? = null,
    val notes: String? = null,
)
