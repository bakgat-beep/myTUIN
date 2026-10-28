package com.mytuin.gardenplanner.ui.garden

/**
 * Per-field validation errors for the spatial object form (Step 9e).
 *
 * Only geometry can be invalid, and only in CREATE mode (EDIT is
 * scalar-only). All other fields are optional by domain design:
 * name, description, notes are nullable; objectType always has a
 * value; areaId may be null (B2).
 */
data class SpatialObjectFormErrors(
    val geometryInvalid: Boolean = false,
) {
    val isValid: Boolean
        get() = !geometryInvalid

    companion object {
        val NONE = SpatialObjectFormErrors()
    }
}
