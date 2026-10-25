package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import com.mytuin.gardenplanner.domain.model.location.Country
import com.mytuin.gardenplanner.domain.repository.CountryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

/**
 * Reads countries.json from assets and caches the result in memory.
 *
 * Asset format:
 *   [
 *     {"code": "AF", "name": "Afghanistan"},
 *     ...
 *   ]
 *
 * Localisation (E4 dataset (b)) arrives as countries-<locale>.json
 * later; this implementation reads the default file.
 */
class JsonAssetCountryRepository
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : CountryRepository {
        private var cache: List<Country>? = null

        override suspend fun getCountries(): List<Country> =
            cache ?: withContext(Dispatchers.IO) {
                val json =
                    context.assets
                        .open(ASSET_NAME)
                        .bufferedReader()
                        .use { it.readText() }
                val parsed = parse(json)
                cache = parsed
                parsed
            }

        private fun parse(json: String): List<Country> {
            val array = JSONArray(json)
            val result = ArrayList<Country>(array.length())
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                result +=
                    Country(
                        code = obj.getString("code"),
                        name = obj.getString("name"),
                    )
            }
            return result
        }

        private companion object {
            const val ASSET_NAME = "countries.json"
        }
    }
