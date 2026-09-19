package com.mytuin.gardenplanner.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mytuin.gardenplanner.data.datastore.DisplayPreferencesKeys
import com.mytuin.gardenplanner.domain.display.DisplayPreferences
import com.mytuin.gardenplanner.domain.display.ThemeMode
import com.mytuin.gardenplanner.domain.display.UnitSystem
import com.mytuin.gardenplanner.domain.repository.DisplayPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore-backed display preferences.
 *
 * DEC-042: display preferences are per-device, not exported, not
 * migrated by the garden schema mechanism.
 *
 * Reading maps stored canonical ids back to enums. An unrecognised id
 * falls back to the enum default rather than throwing: a display
 * preference is not a data-integrity concern in the way a stored
 * garden record is. If a user downgrades the app to a version that
 * does not know a value a newer version wrote, the preference resets
 * to default rather than crashing the settings screen.
 *
 * That differs deliberately from the Room TypeConverters, which call
 * error(...) on unknown ids. The Room converters guard garden data;
 * this guards a per-device UI setting.
 */
class DisplayPreferencesRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : DisplayPreferencesRepository {
        override val preferences: Flow<DisplayPreferences> =
            dataStore.data.map { prefs -> prefs.toDisplayPreferences() }

        override suspend fun setThemeMode(mode: ThemeMode) {
            dataStore.edit { prefs ->
                prefs[DisplayPreferencesKeys.THEME_MODE] = mode.id
            }
        }

        override suspend fun setUnitSystem(system: UnitSystem) {
            dataStore.edit { prefs ->
                prefs[DisplayPreferencesKeys.UNIT_SYSTEM] = system.id
            }
        }

        override suspend fun setLanguageCode(code: String) {
            dataStore.edit { prefs ->
                prefs[DisplayPreferencesKeys.LANGUAGE_CODE] = code
            }
        }
    }

private fun Preferences.toDisplayPreferences(): DisplayPreferences =
    DisplayPreferences(
        themeMode =
            this[DisplayPreferencesKeys.THEME_MODE]
                ?.let { ThemeMode.fromId(it) }
                ?: ThemeMode.DEFAULT,
        unitSystem =
            this[DisplayPreferencesKeys.UNIT_SYSTEM]
                ?.let { UnitSystem.fromId(it) }
                ?: UnitSystem.DEFAULT,
        languageCode =
            this[DisplayPreferencesKeys.LANGUAGE_CODE]
                ?: DisplayPreferences.DEFAULT_LANGUAGE_CODE,
    )
