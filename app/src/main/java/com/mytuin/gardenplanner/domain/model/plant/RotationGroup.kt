package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.RotationGroupType

/**
 * RotationGroup.
 *
 * PLANNING_VOCABULARIES §17. VOCABULARY_INDEX §3.3.
 *
 * A crop-rotation classification. System groups are application-wide
 * (gardenId null); user-defined groups belong to one garden
 * (gardenId non-null). The mapper enforces this correspondence
 * (P41E-1 (c)).
 *
 * A plant's association to a rotation group is stored as a
 * PlantMultiValue.Rotation row (P41E-2 (b)), not as an FK on Plant.
 */
data class RotationGroup(
    val id: String,
    val name: String,
    val groupType: RotationGroupType,
    val status: RecordStatus,
    val gardenId: String?,
    val description: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
