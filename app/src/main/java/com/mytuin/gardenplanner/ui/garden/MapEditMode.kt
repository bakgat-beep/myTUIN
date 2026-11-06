package com.mytuin.gardenplanner.ui.garden

/**
 * Interaction mode for the map editor (Step 9g).
 *
 * SELECT — tap a shape to select, drag the shape body to move,
 * drag a vertex to reshape, tap Delete / Undo on the control panel.
 * Also supports long-press to delete a vertex or insert one at an
 * edge midpoint.
 *
 * DRAW — tap on the map to place vertices for a new outline of the
 * currently selected shape. Close ring to save (replaces geometry,
 * writes history), Clear to start over, Cancel to exit draw mode.
 *
 * DRAW requires a selection; the composable exits Draw mode
 * automatically when the selection clears.
 */
enum class MapEditMode {
    SELECT,
    DRAW,
}
