package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere

/**
 * The user-settable fields for a new Garden.
 *
 * Used as the input to CreateGarden. The use case assigns id,
 * createdAt, updatedAt and status; those are intentionally absent
 * here so the caller cannot supply them.
 *
 * countryCode is ISO 3166-1 alpha-2 (D9).
 * hemisphere defaults to UNKNOWN (D4). CORE_VOCABULARIES §8 notes
 * that hemisphere should normally be derived from the garden's
 * geographic context where possible; NewGarden accepts it as an
 * optional explicit value for the case where it is not derivable.
 */
data class NewGarden(
    val name: String,
    val description: String? = null,
    val countryCode: String? = null,
    val region: String? = null,
    val locality: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezone: String? = null,
    val hemisphere: Hemisphere = Hemisphere.UNKNOWN,
)