package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.location.Country
import com.mytuin.gardenplanner.domain.repository.CountryRepository

class FakeCountryRepository(
    private val countries: List<Country> =
        listOf(
            Country(code = "AU", name = "Australia"),
            Country(code = "NZ", name = "New Zealand"),
            Country(code = "GB", name = "United Kingdom"),
        ),
) : CountryRepository {
    override suspend fun getCountries(): List<Country> = countries
}
