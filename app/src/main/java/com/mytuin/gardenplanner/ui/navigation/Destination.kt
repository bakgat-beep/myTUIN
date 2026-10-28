package com.mytuin.gardenplanner.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Yard
import androidx.compose.ui.graphics.vector.ImageVector
import com.mytuin.gardenplanner.R

sealed class Destination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    data object Home : Destination("home", R.string.nav_home, Icons.Filled.Home)

    data object Garden : Destination("garden", R.string.nav_garden, Icons.Filled.Yard)

    data object Plants : Destination("plants", R.string.nav_plants, Icons.Filled.Eco)

    data object Planner : Destination("planner", R.string.nav_planner, Icons.Filled.EditCalendar)

    data object Inbox : Destination("inbox", R.string.nav_inbox, Icons.Filled.Inbox)

    companion object {
        val all: List<Destination> = listOf(Home, Garden, Plants, Planner, Inbox)
    }
}

object Routes {
    const val CREATE_GARDEN: String = "create_garden"

    const val GARDEN_DETAIL_ARG_GARDEN_ID: String = "gardenId"
    const val GARDEN_DETAIL_PATTERN: String = "garden_detail/{$GARDEN_DETAIL_ARG_GARDEN_ID}"

    const val GROWING_SPACES_ARG_GARDEN_ID: String = "gardenId"
    const val GROWING_SPACES_PATTERN: String = "growing_spaces/{$GROWING_SPACES_ARG_GARDEN_ID}"

    const val GROWING_SPACE_DETAIL_ARG_ID: String = "growingSpaceId"
    const val GROWING_SPACE_DETAIL_PATTERN: String = "growing_space/{$GROWING_SPACE_DETAIL_ARG_ID}"

    const val CREATE_GROWING_SPACE_ARG_GARDEN_ID: String = "gardenId"
    const val CREATE_GROWING_SPACE_PATTERN: String =
        "create_growing_space/{$CREATE_GROWING_SPACE_ARG_GARDEN_ID}"

    const val EDIT_GROWING_SPACE_ARG_ID: String = "growingSpaceId"
    const val EDIT_GROWING_SPACE_PATTERN: String =
        "edit_growing_space/{$EDIT_GROWING_SPACE_ARG_ID}"

    fun gardenDetail(gardenId: String): String = "garden_detail/$gardenId"

    fun growingSpaces(gardenId: String): String = "growing_spaces/$gardenId"

    fun growingSpaceDetail(growingSpaceId: String): String = "growing_space/$growingSpaceId"

    fun createGrowingSpace(gardenId: String): String = "create_growing_space/$gardenId"

    fun editGrowingSpace(growingSpaceId: String): String = "edit_growing_space/$growingSpaceId"

    const val SPATIAL_OBJECTS_ARG_GARDEN_ID: String = "gardenId"
    const val SPATIAL_OBJECTS_PATTERN: String = "spatial_objects/{$SPATIAL_OBJECTS_ARG_GARDEN_ID}"

    const val SPATIAL_OBJECT_DETAIL_ARG_ID: String = "spatialObjectId"
    const val SPATIAL_OBJECT_DETAIL_PATTERN: String = "spatial_object/{$SPATIAL_OBJECT_DETAIL_ARG_ID}"

    const val CREATE_SPATIAL_OBJECT_ARG_GARDEN_ID: String = "gardenId"
    const val CREATE_SPATIAL_OBJECT_PATTERN: String =
        "create_spatial_object/{$CREATE_SPATIAL_OBJECT_ARG_GARDEN_ID}"

    const val EDIT_SPATIAL_OBJECT_ARG_ID: String = "spatialObjectId"
    const val EDIT_SPATIAL_OBJECT_PATTERN: String =
        "edit_spatial_object/{$EDIT_SPATIAL_OBJECT_ARG_ID}"

    fun spatialObjects(gardenId: String): String = "spatial_objects/$gardenId"

    fun spatialObjectDetail(spatialObjectId: String): String = "spatial_object/$spatialObjectId"

    fun createSpatialObject(gardenId: String): String = "create_spatial_object/$gardenId"

    fun editSpatialObject(spatialObjectId: String): String = "edit_spatial_object/$spatialObjectId"
}
