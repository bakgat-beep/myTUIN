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
 * The five V1 primary destinations shown in the bottom navigation bar.
 *
 * D9a-6 (b): non-tab routes live in [Routes], not in this sealed
 * class. `Destination.all` is the bottom-nav item list and stays that
 * way.
 */
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
 */
object Routes {
    const val CREATE_GARDEN: String = "create_garden"
}
