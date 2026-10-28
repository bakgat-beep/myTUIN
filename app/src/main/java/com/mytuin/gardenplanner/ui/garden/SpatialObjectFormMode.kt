package com.mytuin.gardenplanner.ui.garden

/**
 * Create vs edit mode for the shared spatial object form.
 *
 * CREATE requires a rectangle geometry (A5: SpatialObject.geometry is
 * non-null). EDIT does not touch geometry (Step 9e: scalar-only edit,
 * deferred geometry to 9g with history decision). The two modes
 * therefore expose different validation and produce different
 * form-data payloads.
 */
enum class SpatialObjectFormMode {
    CREATE,
    EDIT,
}
