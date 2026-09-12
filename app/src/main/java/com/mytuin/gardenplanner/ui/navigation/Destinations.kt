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

/**
 * The five V1 primary destinations.
 *
 * V1_SCREEN_SPECIFICATION §3, §81; PHASE_0_PROJECT_FOUNDATION §25.
 * Route strings are internal navigation identifiers, not controlled
 * vocabulary. Labels are localisation resources.
 */
sealed class Destination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
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