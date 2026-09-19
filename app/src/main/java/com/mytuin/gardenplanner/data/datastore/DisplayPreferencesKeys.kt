package com.mytuin.gardenplanner.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Preference keys for the display DataStore.
 *
 * Keys are internal to the data layer; callers use the repository
 * interface and domain enums. If a key name ever changes, a DataStore
 * migration is required — the same discipline as a Room column rename.
 */
internal object DisplayPreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val UNIT_SYSTEM = stringPreferencesKey("unit_system")
    val LANGUAGE_CODE = stringPreferencesKey("language_code")
}
