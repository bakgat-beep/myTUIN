package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere

/**
 * User-settable location fields for a Garden.
 *
 * A121=a. Used as the input to UpdateGardenLocation. The use case
 * passes these through to the repository, which replaces all seven
 * fields on the current row. Fields supplied as null overwrite any
 * existing value — this is a replace, not a patch. Consistent with
 * UpdateGrowingSpaceGeometry's behaviour for geometry.
 *
 * countryCode is ISO 3166-1 alpha-2 (D9).
 * latitude and longitude are WGS84 decimal degrees, unchanged from
 * CreateGarden and GardenEntity.
 *
 * hemisphere is explicit input (A122=a). CORE_VOCABULARIES §8 says
 * hemisphere should normally be derived where reliable location
 * information already establishes it, but the derivation rule is
 * not specified. Deriving it here would be inventing vocabulary
 * semantics.
 */
data class NewGardenLocation(
    val countryCode: String? = null,
    val region: String? = null,
    val locality: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezone: String? = null,
    val hemisphere: Hemisphere = Hemisphere.UNKNOWN,
)