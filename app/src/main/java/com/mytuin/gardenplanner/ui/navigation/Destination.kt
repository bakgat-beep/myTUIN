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

/**
 * Routes reachable from the shell but not shown in the bottom bar.
 *
 * Garden Detail (9b) carries a gardenId argument. Editing location is
 * a sheet within Detail (E1), not a route — see the 9b pre-code
 * statement for the deviation from E10.
 */
object Routes {
    const val CREATE_GARDEN: String = "create_garden"

    const val GARDEN_DETAIL_ARG_GARDEN_ID: String = "gardenId"
    const val GARDEN_DETAIL_PATTERN: String = "garden_detail/{$GARDEN_DETAIL_ARG_GARDEN_ID}"

    fun gardenDetail(gardenId: String): String = "garden_detail/$gardenId"
}
