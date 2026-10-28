package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields

/**
 * Validated output of [GrowingSpaceFormState.toFormData].
 *
 * Passed from the screen to the viewmodel on save. Represents
 * everything the viewmodel needs to call the use cases.
 */
data class GrowingSpaceFormData(
    val fields: GrowingSpaceFields,
    val geometryEdit: GeometryEdit,
)
