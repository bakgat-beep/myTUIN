package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.location.Country

/**
 * Provides the country reference list.
 *
 * Synchronous getter: the list is small, static, and read once per
 * screen. No Flow needed.
 */
interface CountryRepository {
    suspend fun getCountries(): List<Country>
}
