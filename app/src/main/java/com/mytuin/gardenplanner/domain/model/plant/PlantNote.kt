package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.PlantNoteKind

/**
 * A free-text note attached to a Plant.
 *
 * PLANT_VOCABULARIES §41 (note fields). Stored in the plant_note
 * table. One row per (plant, kind).
 *
 * Text is user-entered and receives strong preservation guarantees
 * (CONTRIBUTION_AND_DEVELOPMENT_GUIDELINES §20). No length limit
 * and no content validation (P41C-7).
 *
 * `rotation_notes` is not a valid kind in this step; it arrives with
 * the rotation_group reference entity in step 4e (P41C-9).
 */
data class PlantNote(
    val plantId: String,
    val noteKind: PlantNoteKind,
    val text: String,
)
