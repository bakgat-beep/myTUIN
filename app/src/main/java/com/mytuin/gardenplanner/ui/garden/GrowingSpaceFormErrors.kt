package com.mytuin.gardenplanner.ui.garden

/**
 * Per-field validation errors for the growing space form.
 *
 * All flags false means the form is valid. Each flag independently
 * highlights the corresponding field when true.
 */
data class GrowingSpaceFormErrors(
    val nameBlank: Boolean = false,
    val lengthInvalid: Boolean = false,
    val widthInvalid: Boolean = false,
    val heightInvalid: Boolean = false,
    val diameterInvalid: Boolean = false,
    val areaInvalid: Boolean = false,
    val volumeInvalid: Boolean = false,
    val geometryInvalid: Boolean = false,
) {
    val isValid: Boolean
        get() =
            !nameBlank &&
                !lengthInvalid &&
                !widthInvalid &&
                !heightInvalid &&
                !diameterInvalid &&
                !areaInvalid &&
                !volumeInvalid &&
                !geometryInvalid

    companion object {
        val NONE = GrowingSpaceFormErrors()
    }
}
