package com.mytuin.gardenplanner.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.garden.GardenScreen
import com.mytuin.gardenplanner.ui.home.HomeScreen
import com.mytuin.gardenplanner.ui.inbox.InboxScreen
import com.mytuin.gardenplanner.ui.planner.PlannerScreen
import com.mytuin.gardenplanner.ui.plants.PlantsScreen
import com.mytuin.gardenplanner.ui.theme.Spacing

@Composable
fun MyTuinNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                Destination.all.forEach { destination ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == destination.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(destination.labelRes)) }
                    )
                }
            }
        },
        floatingActionButton = { AddActionFab() }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) { HomeScreen() }
            composable(Destination.Garden.route) { GardenScreen() }
            composable(Destination.Plants.route) { PlantsScreen() }
            composable(Destination.Planner.route) { PlannerScreen() }
            composable(Destination.Inbox.route) { InboxScreen() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddActionFab() {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    FloatingActionButton(onClick = { showSheet = true }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_action_content_description)
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            AddActionSheet(onDismiss = { showSheet = false })
        }
    }
}

@Composable
private fun AddActionSheet(onDismiss: () -> Unit) {
    // V1_SCREEN_SPECIFICATION §4.1 — seven primary options plus More.
    // Step 1: inert. Each row dismisses the sheet.
    val options = listOf(
        R.string.add_option_plant,
        R.string.add_option_water,
        R.string.add_option_harvest,
        R.string.add_option_observe,
        R.string.add_option_feed,
        R.string.add_option_prune,
        R.string.add_option_intervention,
        R.string.add_option_more,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.screenMargin)
            .padding(bottom = Spacing.xl)
    ) {
        Text(
            text = stringResource(R.string.add_sheet_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = Spacing.m)
        )
        options.forEach { labelRes ->
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.xs)
            ) {
                Text(
                    text = stringResource(labelRes),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}