package com.mytuin.gardenplanner.domain.display

/**
 * Display preferences.
 *
 * DEC-042: per-device, not part of garden data, excluded from export.
 *
 * S1: languageCode is a plain ISO 639-1 string, not a vocabulary.
 * The app ships English; Afrikaans is planned. A value class or enum
 * would require deciding the full language list now, which the spec
 * does not do. Defaults to English.
 */
data class DisplayPreferences(
    val themeMode: ThemeMode,
    val unitSystem: UnitSystem,
    val languageCode: String,
) {
    companion object {
        const val DEFAULT_LANGUAGE_CODE: String = "en"

        val DEFAULTS: DisplayPreferences = DisplayPreferences(
            themeMode = ThemeMode.DEFAULT,
            unitSystem = UnitSystem.DEFAULT,
            languageCode = DEFAULT_LANGUAGE_CODE,
        )
    }
}