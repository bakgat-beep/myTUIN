package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType

/**
 * Initial values for the growing space form.
 *
 * Bundles everything into one argument so the state holder can take
 * a single seed rather than fifteen parameters (same pattern as
 * LocationFormState in step 9b).
 *
 * The `xxxText` fields hold the raw strings the user sees. The seed
 * converts from domain models (via the companion helpers) so the
 * form does not need to re-derive display strings from Doubles on
 * every recomposition.
 */
data class GrowingSpaceFormSeed(
    val name: String = "",
    val spaceType: GrowingSpaceType = GrowingSpaceType.GARDEN_BED,
    val description: String = "",
    val lengthText: String = "",
    val widthText: String = "",
    val heightText: String = "",
    val diameterText: String = "",
    val areaText: String = "",
    val volumeText: String = "",
    val notes: String = "",
    val rectX1: String = "",
    val rectY1: String = "",
    val rectX2: String = "",
    val rectY2: String = "",
) {
    companion object {
        fun fromSpace(space: GrowingSpace): GrowingSpaceFormSeed =
            GrowingSpaceFormSeed(
                name = space.name,
                spaceType = space.spaceType,
                description = space.description ?: "",
                lengthText = space.lengthMetres?.toString() ?: "",
                widthText = space.widthMetres?.toString() ?: "",
                heightText = space.heightMetres?.toString() ?: "",
                diameterText = space.diameterMetres?.toString() ?: "",
                areaText = space.areaSquareMetres?.toString() ?: "",
                volumeText = space.volumeCubicMetres?.toString() ?: "",
                notes = space.notes ?: "",
            )
    }
}
