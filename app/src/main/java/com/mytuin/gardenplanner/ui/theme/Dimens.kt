package com.mytuin.gardenplanner.ui.theme

import androidx.compose.ui.unit.dp

/** Spacing scale — V1_VISUAL_DESIGN_SPECIFICATION §13. */
object Spacing {
    val xs = 4.dp
    val s = 8.dp
    val m = 12.dp
    val l = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 40.dp
    val huge = 48.dp

    /** Standard content margin — §14. */
    val screenMargin = 16.dp
}

/** Corner radii — §17, §62. */
object Radius {
    val standard = 12.dp
}

/** Touch target minimum — §58. */
object TouchTarget {
    val minimum = 48.dp
}

/** Elevation — §18. */
object Elevation {
    val flat = 0.dp
    val raised = 4.dp
}