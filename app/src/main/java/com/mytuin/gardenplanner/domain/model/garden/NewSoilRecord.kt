package com.mytuin.gardenplanner.domain.model.garden

/**
 * User-settable fields for a new SoilRecord.
 *
 * The use case assigns id, status, createdAt and updatedAt.
 *
 * At most one of areaId and growingSpaceId may be supplied
 * (S5-1 (a)). The mapper enforces the invariant on read; the
 * repository's pre-checks reject the case where a nonexistent
 * parent is referenced.
 */
data class NewSoilRecord(
    val gardenId: String,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val name: String? = null,
    val notes: String? = null,
)
