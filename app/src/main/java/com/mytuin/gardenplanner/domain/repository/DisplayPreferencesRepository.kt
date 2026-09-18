package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.display.DisplayPreferences
import com.mytuin.gardenplanner.domain.display.ThemeMode
import com.mytuin.gardenplanner.domain.display.UnitSystem
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for display preferences.
 *
 * DEC-042. A171 split: this is the DataStore side (step 6j). The Room
 * side for garden-affecting preferences is step 6i.
 *
 * S4: a single Flow<DisplayPreferences>, not three separate flows.
 *
 * No production consumer yet. When the settings UI or the theme
 * selection lands, it becomes the first.
 *
 * S2: display preferences are excluded from export per DEC-042. Any
 * future export step must not call this repository.
 */
interface DisplayPreferencesRepository {

    val preferences: Flow<DisplayPreferences>

    suspend fun setThemeMode(mode: ThemeMode)

    suspend fun setUnitSystem(system: UnitSystem)

    suspend fun setLanguageCode(code: String)
}