package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Plant.
 *
 * V1_DATABASE_SCHEMA.md §16. DATA_MODEL.md §12.
 *
 * Plant is reference/knowledge data. It does not represent anything
 * growing in the user's garden (CORE_ARCHITECTURE §10, §11;
 * V1_DATABASE_SCHEMA §75 Invariant 1).
 *
 * Stable identifier format: "plant_<uuid>" (D1).
 *
 * lifecycle is nullable rather than defaulting to UNKNOWN. PLANT_VOCABULARIES
 * §3 provides an UNKNOWN value, but "no lifecycle recorded" and
 * "lifecycle recorded as unknown" are distinguishable concerns
 * (CORE_VOCABULARIES §4). Nullable keeps the distinction available
 * for the later information_state work; the enum can still carry
 * UNKNOWN explicitly when the data says so.
 *
 * Field names use snake_case to match the schema document. Entities
 * are persistence-layer types, not domain models (V1_TECHNICAL_ARCHITECTURE
 * §79).
 */
@Entity(tableName = "plant")
data class PlantEntity(
    @PrimaryKey
    val id: String,

    val canonical_name: String,

    val created_at: Long,

    val updated_at: Long,

    val status: RecordStatus,

    val scientific_name: String? = null,

    val genus: String? = null,

    val species: String? = null,

    val family: String? = null,

    val lifecycle: PlantLifecycle? = null,

    val description: String? = null,
)