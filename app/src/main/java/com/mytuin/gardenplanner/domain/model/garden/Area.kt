package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Area — domain model.
 *
 * V1_DATABASE_SCHEMA.md §12. DATA_MODEL.md §7.
 *
 * Areas are organisational as well as geographic (DATA_MODEL §7).
 * geometry is optional: an Area may be purely organisational.
 *
 * status is nullable (A6). V1_DATABASE_SCHEMA §12 lists it as
 * optional, meaning an Area row may exist with no lifecycle state.
 * That is distinct from "recorded as unknown" (CORE_VOCABULARIES §4).
 * DAO filters treat a null status as not-archived.
 */
data class Area(
    val id: String,
    val gardenId: String,
    val name: String,
    val areaType: AreaType,
    val description: String?,
    val geometry: Geometry?,
    val status: RecordStatus?,
    val createdAt: Long,
    val updatedAt: Long,
)
