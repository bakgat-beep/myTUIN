package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry

/**
 * The geometry change requested by the form, if any.
 *
 * The form offers an optional rectangle editor; leaving it blank
 * means "keep current geometry" on edit and "no geometry" on create.
 * Filling in four corners produces Replace with a closed polygon.
 *
 * There is no Clear in 9d: clearing an existing geometry would be
 * expressed by the rectangle form being used to specify a
 * different, valid rectangle. Explicit clearing is deferred to
 * 9g's map editor.
 */
sealed interface GeometryEdit {
    data object Unchanged : GeometryEdit

    data class Replace(
        val geometry: Geometry,
    ) : GeometryEdit
}
