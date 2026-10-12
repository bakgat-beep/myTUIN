package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantNoteEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantNote

fun PlantNoteEntity.toDomain(): PlantNote =
    PlantNote(
        plantId = plant_id,
        noteKind = note_kind,
        text = text,
    )
