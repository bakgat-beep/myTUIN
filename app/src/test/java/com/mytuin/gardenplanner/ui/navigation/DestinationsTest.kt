package com.mytuin.gardenplanner.ui.navigation

import org.junit.Assert
import org.junit.Test

class DestinationsTest {
    @Test
    fun `all contains the five V1 destinations in specification order`() {
        val routes = Destination.all.map { it.route }
        Assert.assertEquals(
            listOf("home", "garden", "plants", "planner", "inbox"),
            routes,
        )
    }

    @Test
    fun `routes are unique`() {
        val routes = Destination.all.map { it.route }
        Assert.assertEquals(routes.size, routes.toSet().size)
    }

    @Test
    fun `label resources are unique`() {
        val labels = Destination.all.map { it.labelRes }
        Assert.assertEquals(labels.size, labels.toSet().size)
    }
}
