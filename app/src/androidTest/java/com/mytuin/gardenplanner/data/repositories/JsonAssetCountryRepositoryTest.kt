package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JsonAssetCountryRepositoryTest {
    private lateinit var repository: JsonAssetCountryRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repository = JsonAssetCountryRepository(context)
    }

    @After
    fun tearDown() {
        // no resource to release
    }

    @Test
    fun countries_asset_loads_all_entries() =
        runBlocking {
            val countries = repository.getCountries()
            assertTrue(
                "Expected at least 240 countries, got ${countries.size}",
                countries.size >= 240,
            )
        }

    @Test
    fun each_country_has_two_uppercase_letters_and_non_blank_name() =
        runBlocking {
            val countries = repository.getCountries()
            countries.forEach { country ->
                assertEquals(
                    "Code must be two characters: '${country.code}'",
                    2,
                    country.code.length,
                )
                assertTrue(
                    "Code must be uppercase letters: '${country.code}'",
                    country.code.all { it in 'A'..'Z' },
                )
                assertTrue(
                    "Name must not be blank for ${country.code}",
                    country.name.isNotBlank(),
                )
            }
        }

    @Test
    fun no_duplicate_codes() =
        runBlocking {
            val countries = repository.getCountries()
            val codes = countries.map { it.code }
            assertEquals(
                "Duplicate codes: " +
                    codes.groupBy { it }.filterValues { it.size > 1 }.keys,
                codes.size,
                codes.toSet().size,
            )
        }

    @Test
    fun new_zealand_and_australia_present() =
        runBlocking {
            val countries = repository.getCountries()
            val codes = countries.map { it.code }.toSet()
            assertTrue("NZ must exist", "NZ" in codes)
            assertTrue("AU must exist", "AU" in codes)
            assertTrue("US must exist", "US" in codes)
            assertTrue("GB must exist", "GB" in codes)
        }

    @Test
    fun second_call_returns_cached_list() =
        runBlocking {
            val first = repository.getCountries()
            val second = repository.getCountries()
            assertTrue(
                "Cache must return the same instance",
                first === second,
            )
        }
}
