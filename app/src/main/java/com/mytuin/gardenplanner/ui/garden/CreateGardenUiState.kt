package com.mytuin.gardenplanner.ui.garden

/**
 * State for the Create Garden screen.
 *
 * Errors are modelled as sealed enums so the ViewModel carries no
 * user-facing strings; the composable resolves via stringResource.
 * Tests assert against the enum, not against text.
 *
 * `savedGardenId` is the one-shot navigation signal (D9a-4 (a)). The
 * UI observes for non-null, calls onSaveHandled() to clear, then
 * navigates.
 */
data class CreateGardenUiState(
    val name: String = "",
    val description: String = "",
    val nameError: NameError? = null,
    val isSaving: Boolean = false,
    val saveError: SaveError? = null,
    val savedGardenId: String? = null,
) {
    enum class NameError {
        BLANK,
    }

    enum class SaveError {
        GENERIC,
    }
}
