package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType

/**
 * PlantAlias.
 *
 * V1_DATABASE_SCHEMA.md §17. DATA_MODEL.md §13.
 *
 * Stable identifier format: "plantalias_<uuid>" (D1).
 *
 * Foreign key to Plant is RESTRICT (A6): a Plant may not be deleted
 * while aliases reference it. V1_DATABASE_SCHEMA §8 prefers
 * deprecation over deletion for knowledge records; RESTRICT enforces
 * that at the database boundary.
 *
 * Uniqueness: one row per (plant_id, alias) pair (A12). The composite
 * unique index serves both the uniqueness constraint and the FK
 * lookup path (A15); a separate plant_id index would be redundant.
 *
 * language is optional ISO 639-1 (A10). Language is data, not
 * vocabulary.
 */
@Entity(
    tableName = "plant_alias",
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
        Index(value = ["plant_id", "alias"], unique = true),
    ],
)
data class PlantAliasEntity(
    @PrimaryKey
    val id: String,

    val plant_id: String,

    val alias: String,

    val alias_type: PlantAliasType,

    val language: String? = null,
)