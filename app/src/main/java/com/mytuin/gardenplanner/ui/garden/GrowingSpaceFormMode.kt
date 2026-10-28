package com.mytuin.gardenplanner.ui.garden

/**
 * Create vs edit mode for the shared growing space form.
 *
 * The only behavioural difference is the presence of a "current
 * geometry" line and its help text above the rectangle fields in
 * edit mode. All other fields are identical between modes.
 */
enum class GrowingSpaceFormMode {
    CREATE,
    EDIT,
}
