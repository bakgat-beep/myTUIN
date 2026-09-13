package com.mytuin.gardenplanner.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenDatabaseMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        GardenDatabase::class.java,
    )

    @Test
    fun empty_database_fixture_opens_at_v1() {
        helper.createDatabase("empty-fixture", 1).close()
    }

    @Test
    fun minimal_garden_fixture_opens_at_v1() {
        helper.createDatabase("minimal-garden-fixture", 1).close()
    }

    @Test
    fun representative_garden_fixture_opens_at_v1() {
        helper.createDatabase("representative-garden-fixture", 1).close()
    }
}