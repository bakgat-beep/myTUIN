package com.mytuin.gardenplanner.ui.garden

import androidx.annotation.StringRes
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import java.util.Locale

/**
 * String resources and formatting shared between the growing space
 * list and detail screens (Step 9c).
 */

@StringRes
internal fun spaceTypeLabelRes(type: GrowingSpaceType): Int =
    when (type) {
        GrowingSpaceType.GARDEN_BED -> R.string.growing_space_type_garden_bed
        GrowingSpaceType.RAISED_BED -> R.string.growing_space_type_raised_bed
        GrowingSpaceType.IN_GROUND_BED -> R.string.growing_space_type_in_ground_bed
        GrowingSpaceType.CONTAINER -> R.string.growing_space_type_container
        GrowingSpaceType.POT -> R.string.growing_space_type_pot
        GrowingSpaceType.PLANTER -> R.string.growing_space_type_planter
        GrowingSpaceType.GREENHOUSE -> R.string.growing_space_type_greenhouse
        GrowingSpaceType.POLYTUNNEL -> R.string.growing_space_type_polytunnel
        GrowingSpaceType.ORCHARD -> R.string.growing_space_type_orchard
        GrowingSpaceType.ROW -> R.string.growing_space_type_row
        GrowingSpaceType.BORDER -> R.string.growing_space_type_border
        GrowingSpaceType.VERTICAL_SPACE -> R.string.growing_space_type_vertical_space
        GrowingSpaceType.NURSERY_AREA -> R.string.growing_space_type_nursery_area
        GrowingSpaceType.LAWN -> R.string.growing_space_type_lawn
        GrowingSpaceType.MIXED_AREA -> R.string.growing_space_type_mixed_area
        GrowingSpaceType.OTHER -> R.string.growing_space_type_other
    }

@StringRes
internal fun statusLabelRes(status: RecordStatus): Int =
    when (status) {
        RecordStatus.DRAFT -> R.string.status_draft
        RecordStatus.ACTIVE -> R.string.status_active
        RecordStatus.INACTIVE -> R.string.status_inactive
        RecordStatus.DEPRECATED -> R.string.status_deprecated
        RecordStatus.ARCHIVED -> R.string.status_archived
    }

/**
 * One decimal place, dot separator regardless of device locale.
 * Consistency across screens; locale-aware formatting is a later
 * change (DEC-042 defers unit-aware display).
 */
internal fun formatDimension(value: Double): String = "%.1f".format(Locale.US, value)
