package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.mytuin.gardenplanner.domain.vocabulary.PlantNoteKind

/**
 * PlantNote.
 *
 * PLANT_VOCABULARIES §41 (note fields). Phase 1 step 4c-B.
 *
 * Composite primary key (plant_id, note_kind). The leading column
 * satisfies the FK index requirement.
 *
 * One note per kind (P41C-4). If a plant genuinely needs multiple
 * notes per kind, a single note with subheadings is the V1 answer
 * (§74 deliberate simplifications).
 */
@Entity(
    tableName = "plant_note",
    primaryKeys = ["plant_id", "note_kind"],
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
)
data class PlantNoteEntity(
    val plant_id: String,
    val note_kind: PlantNoteKind,
    val text: String,
)
