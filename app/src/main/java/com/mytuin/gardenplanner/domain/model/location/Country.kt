package com.mytuin.gardenplanner.domain.model.location

/**
 * A country entry from the reference list.
 *
 * Reference data loaded from a bundled JSON asset (E4, dataset (b)).
 * `code` is ISO 3166-1 alpha-2 (D9). `name` is English; localisation
 * arrives as per-locale asset files when needed.
 */
data class Country(
    val code: String,
    val name: String,
)
