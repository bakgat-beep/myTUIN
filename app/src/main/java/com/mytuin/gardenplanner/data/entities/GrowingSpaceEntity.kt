package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * GrowingSpace.
 *
 * V1_DATABASE_SCHEMA.md §13. DATA_MODEL.md §8.
 *
 * Stable identifier format: "growingspace_<uuid>" (A57).
 *
 * Foreign key to Garden is RESTRICT (A55): a Garden may not be deleted
 * while growing spaces reference it (V1_DATABASE_SCHEMA §8).
 * Index on garden_id (A56; V1_DATABASE_SCHEMA §68).
 *
 * geometry_type and geometry_data are paired: both null, or both
 * non-null. The mapper enforces this. geometry_data is a GeoJSON
 * string (A50); coordinates are metres in the garden's local frame
 * (A50b=(i)). The GeoJSON type name and the geometry_type id are
 * different strings ("LineString" vs "line"); the mapper cross-checks
 * them.
 *
 * Dimension columns are metres / square metres / cubic metres
 * (A51=A; DEC-042). No unit column: conversion to display units is
 * a presentation-layer concern.
 *
 * area_id is absent (A54).
 */
@Entity(
    tableName = "growing_space",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
    ],
)
data class GrowingSpaceEntity(
    @PrimaryKey
    val id: String,

    val garden_id: String,

    val name: String,

    val space_type: GrowingSpaceType,

    val status: RecordStatus,

    val created_at: Long,

    val updated_at: Long,

    val geometry_type: GeometryType? = null,

    val geometry_data: String? = null,

    val length: Double? = null,

    val width: Double? = null,

    val height: Double? = null,

    val diameter: Double? = null,

    val area: Double? = null,

    val volume: Double? = null,

    val description: String? = null,

    val notes: String? = null,
)