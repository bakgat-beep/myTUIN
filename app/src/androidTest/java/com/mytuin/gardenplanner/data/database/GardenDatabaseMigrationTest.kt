package com.mytuin.gardenplanner.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenDatabaseMigrationTest {
    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            GardenDatabase::class.java,
        )

    /**
     * Every table the current schema version must contain. Add one
     * entry per new table, in the same commit that adds the table.
     * No other change to this file is required when the schema
     * version bumps.
     */
    private val expectedTables: List<String> =
        listOf(
            "growing_space",
            "growing_space_history",
            "garden_preference",
            "garden_plant_preference",
            "area",
            "spatial_object",
            "plant_instance",
            "plant_instance_history",
            "activity",
            "observation",
            "measurement",
            "harvest",
            "harvest_loss",
            "problem",
            "problem_observation",
            "plan",
            "plan_target",
            "source",
        )

    @Test
    fun empty_database_fixture_migrates_from_v1_to_current() {
        helper.createDatabase("empty-fixture", 1).close()

        val migrated = helper.runMigrationsAndValidate("empty-fixture", GARDEN_DATABASE_VERSION, true)

        expectedTables.forEach { table ->
            migrated
                .query(
                    "SELECT name FROM sqlite_master WHERE type = 'table' AND name = '$table'",
                ).use { cursor ->
                    assertTrue("$table must exist after migration", cursor.moveToFirst())
                }
        }
        migrated.close()
    }

    @Test
    fun minimal_garden_fixture_migrates_from_v1_to_current_with_data_intact() {
        helper.createDatabase("minimal-garden-fixture", 1).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_minimal',
                    'Minimal Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("minimal-garden-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_minimal'")
            .use { cursor ->
                assertTrue("v1 garden row must survive migration", cursor.moveToFirst())
                assertEquals("Minimal Garden", cursor.getString(0))
            }
        migrated.close()
    }

    @Test
    fun representative_garden_fixture_migrates_from_v1_to_current_with_full_row_intact() {
        helper.createDatabase("representative-garden-fixture", 1).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, description, country_code, region, locality,
                    latitude, longitude, timezone, hemisphere,
                    created_at, updated_at, status
                ) VALUES (
                    'garden_test_representative',
                    'Representative Garden',
                    'A garden with all optional fields populated',
                    'NZ',
                    'Canterbury',
                    'Christchurch',
                    -43.5321,
                    172.6362,
                    'Pacific/Auckland',
                    'southern',
                    1700000000000,
                    1700000000000,
                    'draft'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("representative-garden-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                """
                SELECT name, description, country_code, region, locality,
                       latitude, longitude, timezone, hemisphere
                FROM garden
                WHERE id = 'garden_test_representative'
                """.trimIndent(),
            ).use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("Representative Garden", cursor.getString(0))
                assertEquals(
                    "A garden with all optional fields populated",
                    cursor.getString(1),
                )
                assertEquals("NZ", cursor.getString(2))
                assertEquals("Canterbury", cursor.getString(3))
                assertEquals("Christchurch", cursor.getString(4))
                assertEquals(-43.5321, cursor.getDouble(5), 0.0)
                assertEquals(172.6362, cursor.getDouble(6), 0.0)
                assertEquals("Pacific/Auckland", cursor.getString(7))
                assertEquals("southern", cursor.getString(8))
            }
        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v2_to_current_with_growing_space_intact() {
        helper.createDatabase("v2-fixture", 2).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v2',
                    'V2 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space (
                    id, garden_id, name, space_type, status,
                    created_at, updated_at, geometry_type, geometry_data
                ) VALUES (
                    'growingspace_test_v2',
                    'garden_test_v2',
                    'V2 Bed',
                    'raised_bed',
                    'active',
                    1700000000000,
                    1700000000000,
                    'point',
                    '{"type":"Point","coordinates":[1.0,2.0]}'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v2-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                """
                SELECT name, space_type, geometry_type, geometry_data
                FROM growing_space
                WHERE id = 'growingspace_test_v2'
                """.trimIndent(),
            ).use { cursor ->
                assertTrue("v2 growing_space row must survive migration", cursor.moveToFirst())
                assertEquals("V2 Bed", cursor.getString(0))
                assertEquals("raised_bed", cursor.getString(1))
                assertEquals("point", cursor.getString(2))
                assertEquals(
                    """{"type":"Point","coordinates":[1.0,2.0]}""",
                    cursor.getString(3),
                )
            }
        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v3_to_current_with_history_intact() {
        helper.createDatabase("v3-fixture", 3).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v3',
                    'V3 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space (
                    id, garden_id, name, space_type, status,
                    created_at, updated_at
                ) VALUES (
                    'growingspace_test_v3',
                    'garden_test_v3',
                    'V3 Bed',
                    'raised_bed',
                    'active',
                    1700000000000,
                    1700000000000
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space_history (
                    id, growing_space_id, valid_from, valid_to, recorded_at
                ) VALUES (
                    'growingspacehistory_test_v3',
                    'growingspace_test_v3',
                    1700000000000,
                    1700000500000,
                    1700000500000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v3-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                """
                SELECT id, growing_space_id, valid_from, valid_to
                FROM growing_space_history
                WHERE id = 'growingspacehistory_test_v3'
                """.trimIndent(),
            ).use { cursor ->
                assertTrue("v3 history row must survive migration", cursor.moveToFirst())
                assertEquals("growingspacehistory_test_v3", cursor.getString(0))
                assertEquals("growingspace_test_v3", cursor.getString(1))
                assertEquals(1_700_000_000_000L, cursor.getLong(2))
                assertEquals(1_700_000_500_000L, cursor.getLong(3))
            }
        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v4_to_current_adding_area_table_with_existing_data_intact() {
        helper.createDatabase("v4-fixture", 4).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v4',
                    'V4 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space (
                    id, garden_id, name, space_type, status,
                    created_at, updated_at
                ) VALUES (
                    'growingspace_test_v4',
                    'garden_test_v4',
                    'V4 Bed',
                    'raised_bed',
                    'active',
                    1700000000000,
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v4-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query("SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'area'")
            .use { cursor ->
                assertTrue("area table must exist after v4 -> v12 migration", cursor.moveToFirst())
            }

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v4'")
            .use { cursor ->
                assertTrue("v4 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V4 Garden", cursor.getString(0))
            }

        migrated
            .query("SELECT name FROM growing_space WHERE id = 'growingspace_test_v4'")
            .use { cursor ->
                assertTrue("v4 growing_space row must survive migration", cursor.moveToFirst())
                assertEquals("V4 Bed", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v5_to_current_adding_spatial_object_table_with_existing_data_intact() {
        helper.createDatabase("v5-fixture", 5).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v5',
                    'V5 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO area (
                    id, garden_id, name, area_type,
                    created_at, updated_at, status
                ) VALUES (
                    'area_test_v5',
                    'garden_test_v5',
                    'Vegetable Garden',
                    'zone',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v5-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'spatial_object'",
            ).use { cursor ->
                assertTrue(
                    "spatial_object table must exist after v5 -> v12 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query("SELECT name FROM area WHERE id = 'area_test_v5'")
            .use { cursor ->
                assertTrue("v5 area row must survive migration", cursor.moveToFirst())
                assertEquals("Vegetable Garden", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v6_to_current_adding_area_id_to_growing_space_with_data_intact() {
        helper.createDatabase("v6-fixture", 6).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v6',
                    'V6 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space (
                    id, garden_id, name, space_type, status,
                    created_at, updated_at
                ) VALUES (
                    'growingspace_test_v6',
                    'garden_test_v6',
                    'V6 Bed',
                    'raised_bed',
                    'active',
                    1700000000000,
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v6-fixture", GARDEN_DATABASE_VERSION, true)

        var areaIdPresent = false
        migrated
            .query("PRAGMA table_info(growing_space)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    if (cursor.getString(1) == "area_id") {
                        areaIdPresent = true
                        break
                    }
                }
            }
        assertTrue("growing_space.area_id must exist after v6 -> v12 migration", areaIdPresent)

        migrated
            .query(
                """
                SELECT name, area_id
                FROM growing_space
                WHERE id = 'growingspace_test_v6'
                """.trimIndent(),
            ).use { cursor ->
                assertTrue("v6 growing_space row must survive migration", cursor.moveToFirst())
                assertEquals("V6 Bed", cursor.getString(0))
                assertTrue("area_id must default to NULL for the migrated row", cursor.isNull(1))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v7_to_current_adding_plant_instance_table_with_data_intact() {
        helper.createDatabase("v7-fixture", 7).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v7',
                    'V7 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO area (
                    id, garden_id, name, area_type,
                    created_at, updated_at, status
                ) VALUES (
                    'area_test_v7',
                    'garden_test_v7',
                    'Vegetable Garden',
                    'zone',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space (
                    id, garden_id, name, space_type, status,
                    created_at, updated_at, area_id
                ) VALUES (
                    'growingspace_test_v7',
                    'garden_test_v7',
                    'V7 Bed',
                    'raised_bed',
                    'active',
                    1700000000000,
                    1700000000000,
                    'area_test_v7'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v7-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'plant_instance'",
            ).use { cursor ->
                assertTrue(
                    "plant_instance table must exist after v7 -> v12 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query(
                "SELECT name, area_id FROM growing_space WHERE id = 'growingspace_test_v7'",
            ).use { cursor ->
                assertTrue("v7 growing_space row must survive migration", cursor.moveToFirst())
                assertEquals("V7 Bed", cursor.getString(0))
                assertEquals("area_test_v7", cursor.getString(1))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v8_to_current_adding_plant_instance_history_table_with_data_intact() {
        helper.createDatabase("v8-fixture", 8).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v8',
                    'V8 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO plant (
                    id, canonical_name, created_at, updated_at, status
                ) VALUES (
                    'plant_test_v8',
                    'V8 Plant',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO plant_instance (
                    id, garden_id, plant_id, status,
                    created_at, updated_at
                ) VALUES (
                    'plantinstance_test_v8',
                    'garden_test_v8',
                    'plant_test_v8',
                    'active',
                    1700000000000,
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v8-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'plant_instance_history'",
            ).use { cursor ->
                assertTrue(
                    "plant_instance_history table must exist after v8 -> v12 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query("SELECT canonical_name FROM plant WHERE id = 'plant_test_v8'")
            .use { cursor ->
                assertTrue("v8 plant row must survive migration", cursor.moveToFirst())
                assertEquals("V8 Plant", cursor.getString(0))
            }

        migrated
            .query("SELECT id, garden_id, plant_id FROM plant_instance WHERE id = 'plantinstance_test_v8'")
            .use { cursor ->
                assertTrue("v8 plant_instance row must survive migration", cursor.moveToFirst())
                assertEquals("plantinstance_test_v8", cursor.getString(0))
                assertEquals("garden_test_v8", cursor.getString(1))
                assertEquals("plant_test_v8", cursor.getString(2))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v9_to_current_adding_activity_table_with_data_intact() {
        helper.createDatabase("v9-fixture", 9).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v9',
                    'V9 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO growing_space (
                    id, garden_id, name, space_type, status,
                    created_at, updated_at
                ) VALUES (
                    'growingspace_test_v9',
                    'garden_test_v9',
                    'V9 Bed',
                    'raised_bed',
                    'active',
                    1700000000000,
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v9-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'activity'",
            ).use { cursor ->
                assertTrue(
                    "activity table must exist after v9 -> v12 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query("SELECT name FROM growing_space WHERE id = 'growingspace_test_v9'")
            .use { cursor ->
                assertTrue("v9 growing_space row must survive migration", cursor.moveToFirst())
                assertEquals("V9 Bed", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v10_to_current_adding_observation_table_with_data_intact() {
        helper.createDatabase("v10-fixture", 10).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v10',
                    'V10 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO activity (
                    id, garden_id, activity_type, occurred_at, created_at,
                    status
                ) VALUES (
                    'activity_test_v10',
                    'garden_test_v10',
                    'watering',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v10-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'observation'",
            ).use { cursor ->
                assertTrue(
                    "observation table must exist after v10 -> v12 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query("SELECT activity_type FROM activity WHERE id = 'activity_test_v10'")
            .use { cursor ->
                assertTrue("v10 activity row must survive migration", cursor.moveToFirst())
                assertEquals("watering", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v11_to_current_adding_measurement_table_with_data_intact() {
        helper.createDatabase("v11-fixture", 11).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v11',
                    'V11 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO activity (
                    id, garden_id, activity_type, occurred_at, created_at,
                    status
                ) VALUES (
                    'activity_test_v11',
                    'garden_test_v11',
                    'observation',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO observation (
                    id, garden_id, activity_id, observed_at,
                    observation_type, confidence, created_at
                ) VALUES (
                    'observation_test_v11',
                    'garden_test_v11',
                    'activity_test_v11',
                    1700000000000,
                    'plant',
                    'moderate',
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v11-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'measurement'",
            ).use { cursor ->
                assertTrue(
                    "measurement table must exist after v11 -> v12 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query("SELECT observation_type FROM observation WHERE id = 'observation_test_v11'")
            .use { cursor ->
                assertTrue("v11 observation row must survive migration", cursor.moveToFirst())
                assertEquals("plant", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v12_to_current_adding_harvest_table_with_data_intact() {
        helper.createDatabase("v12-fixture", 12).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v12',
                    'V12 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO measurement (
                    id, garden_id, property, value, unit,
                    measured_at_epoch_day, confidence, created_at
                ) VALUES (
                    'measurement_test_v12',
                    'garden_test_v12',
                    'ph',
                    6.7,
                    'ph',
                    20000,
                    'high',
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v12-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'harvest'",
            ).use { cursor ->
                assertTrue(
                    "harvest table must exist after v12 -> v13 migration",
                    cursor.moveToFirst(),
                )
            }

        // harvest must have 12 columns.
        var columnCount = 0
        migrated
            .query("PRAGMA table_info(harvest)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                }
            }
        assertEquals("harvest must have 12 columns", 12, columnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v12'")
            .use { cursor ->
                assertTrue("v12 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V12 Garden", cursor.getString(0))
            }

        migrated
            .query("SELECT property, value FROM measurement WHERE id = 'measurement_test_v12'")
            .use { cursor ->
                assertTrue("v12 measurement row must survive migration", cursor.moveToFirst())
                assertEquals("ph", cursor.getString(0))
                assertEquals(6.7, cursor.getDouble(1), 0.0)
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v13_to_current_adding_harvest_loss_table_with_data_intact() {
        helper.createDatabase("v13-fixture", 13).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v13',
                    'V13 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO harvest (
                    id, garden_id, activity_id, date_epoch_day,
                    quantity, unit, created_at
                ) VALUES (
                    'harvest_test_v13',
                    'garden_test_v13',
                    'activity_test_v13',
                    20000,
                    5.0,
                    'count',
                    1700000000000
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO activity (
                    id, garden_id, activity_type, occurred_at, created_at,
                    status
                ) VALUES (
                    'activity_test_v13',
                    'garden_test_v13',
                    'harvesting',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v13-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'harvest_loss'",
            ).use { cursor ->
                assertTrue(
                    "harvest_loss table must exist after v13 -> v14 migration",
                    cursor.moveToFirst(),
                )
            }

        // harvest_loss must have 13 columns.
        var columnCount = 0
        migrated
            .query("PRAGMA table_info(harvest_loss)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                }
            }
        assertEquals("harvest_loss must have 13 columns", 13, columnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v13'")
            .use { cursor ->
                assertTrue("v13 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V13 Garden", cursor.getString(0))
            }

        migrated
            .query("SELECT quantity, unit FROM harvest WHERE id = 'harvest_test_v13'")
            .use { cursor ->
                assertTrue("v13 harvest row must survive migration", cursor.moveToFirst())
                assertEquals(5.0, cursor.getDouble(0), 0.0)
                assertEquals("count", cursor.getString(1))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v14_to_current_adding_problem_tables_with_data_intact() {
        helper.createDatabase("v14-fixture", 14).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v14',
                    'V14 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO harvest_loss (
                    id, garden_id, date_epoch_day, quantity, unit, created_at
                ) VALUES (
                    'harvestloss_test_v14',
                    'garden_test_v14',
                    20000,
                    3.0,
                    'count',
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v14-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'problem'",
            ).use { cursor ->
                assertTrue(
                    "problem table must exist after v14 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'problem_observation'",
            ).use { cursor ->
                assertTrue(
                    "problem_observation table must exist after v14 migration",
                    cursor.moveToFirst(),
                )
            }

        // problem must have 15 columns.
        var problemColumnCount = 0
        migrated
            .query("PRAGMA table_info(problem)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    problemColumnCount += 1
                }
            }
        assertEquals("problem must have 15 columns", 15, problemColumnCount)

        // problem_observation must have 4 columns.
        var linkColumnCount = 0
        migrated
            .query("PRAGMA table_info(problem_observation)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    linkColumnCount += 1
                }
            }
        assertEquals("problem_observation must have 4 columns", 4, linkColumnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v14'")
            .use { cursor ->
                assertTrue("v14 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V14 Garden", cursor.getString(0))
            }

        migrated
            .query(
                "SELECT quantity, unit FROM harvest_loss WHERE id = 'harvestloss_test_v14'",
            ).use { cursor ->
                assertTrue("v14 harvest_loss row must survive migration", cursor.moveToFirst())
                assertEquals(3.0, cursor.getDouble(0), 0.0)
                assertEquals("count", cursor.getString(1))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v15_to_current_adding_plan_tables_and_activity_plan_id_with_data_intact() {
        helper.createDatabase("v15-fixture", 15).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v15',
                    'V15 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO activity (
                    id, garden_id, activity_type, occurred_at, created_at,
                    status
                ) VALUES (
                    'activity_test_v15',
                    'garden_test_v15',
                    'watering',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v15-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'plan'",
            ).use { cursor ->
                assertTrue(
                    "plan table must exist after v15 migration",
                    cursor.moveToFirst(),
                )
            }

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'plan_target'",
            ).use { cursor ->
                assertTrue(
                    "plan_target table must exist after v15 migration",
                    cursor.moveToFirst(),
                )
            }

        // plan must have 15 columns.
        var planColumnCount = 0
        migrated
            .query("PRAGMA table_info(plan)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    planColumnCount += 1
                }
            }
        assertEquals("plan must have 15 columns", 15, planColumnCount)

        // plan_target must have 5 columns.
        var targetColumnCount = 0
        migrated
            .query("PRAGMA table_info(plan_target)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    targetColumnCount += 1
                }
            }
        assertEquals("plan_target must have 5 columns", 5, targetColumnCount)

        // activity must now have 20 columns, including plan_id.
        var activityColumnCount = 0
        var planIdPresent = false
        migrated
            .query("PRAGMA table_info(activity)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    activityColumnCount += 1
                    if (cursor.getString(1) == "plan_id") {
                        planIdPresent = true
                    }
                }
            }
        assertEquals("activity must have 20 columns", 20, activityColumnCount)
        assertTrue("activity.plan_id must exist", planIdPresent)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v15'")
            .use { cursor ->
                assertTrue("v15 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V15 Garden", cursor.getString(0))
            }

        migrated
            .query(
                "SELECT activity_type, plan_id FROM activity WHERE id = 'activity_test_v15'",
            ).use { cursor ->
                assertTrue("v15 activity row must survive migration", cursor.moveToFirst())
                assertEquals("watering", cursor.getString(0))
                assertTrue("pre-existing activity must have null plan_id", cursor.isNull(1))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v16_to_current_adding_source_table_with_data_intact() {
        helper.createDatabase("v16-fixture", 16).apply {
            execSQL(
                """
                INSERT INTO garden (
                    id, name, created_at, updated_at, status, hemisphere
                ) VALUES (
                    'garden_test_v16',
                    'V16 Garden',
                    1700000000000,
                    1700000000000,
                    'draft',
                    'unknown'
                )
                """.trimIndent(),
            )
            execSQL(
                """
                INSERT INTO plan (
                    id, garden_id, plan_type, status, created_at, updated_at
                ) VALUES (
                    'plan_test_v16',
                    'garden_test_v16',
                    'watering',
                    'planned',
                    1700000000000,
                    1700000000000
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v16-fixture", GARDEN_DATABASE_VERSION, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'source'",
            ).use { cursor ->
                assertTrue(
                    "source table must exist after v16 migration",
                    cursor.moveToFirst(),
                )
            }

        // source must have 15 columns.
        var columnCount = 0
        migrated
            .query("PRAGMA table_info(source)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                }
            }
        assertEquals("source must have 15 columns", 15, columnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v16'")
            .use { cursor ->
                assertTrue("v16 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V16 Garden", cursor.getString(0))
            }

        migrated
            .query("SELECT plan_type FROM plan WHERE id = 'plan_test_v16'")
            .use { cursor ->
                assertTrue("v16 plan row must survive migration", cursor.moveToFirst())
                assertEquals("watering", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v17_to_current_adding_plant_scalar_columns_with_data_intact() {
        helper.createDatabase("v17-fixture", 17).apply {
            execSQL(
                """
                INSERT INTO plant (
                    id, canonical_name, created_at, updated_at, status
                ) VALUES (
                    'plant_test_v17',
                    'V17 Plant',
                    1700000000000,
                    1700000000000,
                    'active'
                )
                """.trimIndent(),
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v17-fixture", GARDEN_DATABASE_VERSION, true)

        // plant must now have 25 columns (11 original + 14 new).
        var columnCount = 0
        val columnNames = mutableListOf<String>()
        migrated
            .query("PRAGMA table_info(plant)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                    columnNames += cursor.getString(1)
                }
            }
        assertEquals("plant must have 25 columns after v17 migration", 25, columnCount)

        // Every new column must be present and nullable.
        listOf(
            "growth_rate",
            "support_requirement",
            "light_requirement",
            "shade_tolerance",
            "temperature_class",
            "frost_sensitivity",
            "heat_tolerance",
            "drought_tolerance",
            "salinity_tolerance",
            "waterlogging_tolerance",
            "maturity",
            "maintenance_demand",
            "pollination_requirement",
            "container_suitable",
        ).forEach { column ->
            assertTrue("plant.$column must exist after v17 migration", column in columnNames)
        }

        // The two new indices must exist.
        val indexNames = mutableListOf<String>()
        migrated
            .query("SELECT name FROM sqlite_master WHERE type = 'index' AND tbl_name = 'plant'")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    indexNames += cursor.getString(0)
                }
            }
        assertTrue(
            "an index on light_requirement must exist",
            indexNames.any { it.contains("light_requirement") },
        )
        assertTrue(
            "an index on frost_sensitivity must exist",
            indexNames.any { it.contains("frost_sensitivity") },
        )

        // The pre-existing row must survive with the new columns NULL.
        migrated
            .query(
                """
                SELECT canonical_name, growth_rate, container_suitable
                FROM plant
                WHERE id = 'plant_test_v17'
                """.trimIndent(),
            ).use { cursor ->
                assertTrue("v17 plant row must survive migration", cursor.moveToFirst())
                assertEquals("V17 Plant", cursor.getString(0))
                assertTrue("growth_rate must default to NULL", cursor.isNull(1))
                assertTrue("container_suitable must default to NULL", cursor.isNull(2))
            }

        migrated.close()
    }
}
