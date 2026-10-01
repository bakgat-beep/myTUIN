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

    @Test
    fun empty_database_fixture_migrates_from_v1_to_v10() {
        helper.createDatabase("empty-fixture", 1).close()

        val migrated = helper.runMigrationsAndValidate("empty-fixture", 10, true)

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
        ).forEach { table ->
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
    fun minimal_garden_fixture_migrates_from_v1_to_v10_with_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("minimal-garden-fixture", 10, true)

        migrated
            .query(
                "SELECT name FROM garden WHERE id = 'garden_test_minimal'",
            ).use { cursor ->
                assertTrue("v1 garden row must survive migration", cursor.moveToFirst())
                assertEquals("Minimal Garden", cursor.getString(0))
            }
        migrated.close()
    }

    @Test
    fun representative_garden_fixture_migrates_from_v1_to_v10_with_full_row_intact() {
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

        val migrated =
            helper.runMigrationsAndValidate("representative-garden-fixture", 10, true)

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
    fun fixture_migrates_from_v2_to_v10_with_growing_space_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v2-fixture", 10, true)

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
    fun fixture_migrates_from_v3_to_v10_with_history_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v3-fixture", 10, true)

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
    fun fixture_migrates_from_v4_to_v10_adding_area_table_with_existing_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v4-fixture", 10, true)

        migrated
            .query("SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'area'")
            .use { cursor ->
                assertTrue("area table must exist after v4 -> v7 migration", cursor.moveToFirst())
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
    fun fixture_migrates_from_v5_to_v10_adding_spatial_object_table_with_existing_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v5-fixture", 10, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'spatial_object'",
            ).use { cursor ->
                assertTrue(
                    "spatial_object table must exist after v5 -> v8 migration",
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
    fun fixture_migrates_from_v6_to_v10_adding_area_id_to_growing_space_with_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v6-fixture", 10, true)

        // area_id column must exist on growing_space after migration.
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
        assertTrue("growing_space.area_id must exist after v6 -> v7 migration", areaIdPresent)

        // Existing rows must survive, with area_id NULL for the pre-
        // existing row.
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

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v6'")
            .use { cursor ->
                assertTrue("v6 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V6 Garden", cursor.getString(0))
            }

        migrated.close()
    }

    @Test
    fun fixture_migrates_from_v7_to_v10_adding_plant_instance_table_with_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v7-fixture", 10, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'plant_instance'",
            ).use { cursor ->
                assertTrue(
                    "plant_instance table must exist after v7 -> v8 migration",
                    cursor.moveToFirst(),
                )
            }

        // plant_instance must have 24 columns.
        var columnCount = 0
        migrated
            .query("PRAGMA table_info(plant_instance)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                }
            }
        assertEquals("plant_instance must have 24 columns", 24, columnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v7'")
            .use { cursor ->
                assertTrue("v7 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V7 Garden", cursor.getString(0))
            }

        migrated
            .query("SELECT name FROM area WHERE id = 'area_test_v7'")
            .use { cursor ->
                assertTrue("v7 area row must survive migration", cursor.moveToFirst())
                assertEquals("Vegetable Garden", cursor.getString(0))
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
    fun fixture_migrates_from_v8_to_v10_adding_plant_instance_history_table_with_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v8-fixture", 10, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'plant_instance_history'",
            ).use { cursor ->
                assertTrue(
                    "plant_instance_history table must exist after v8 -> v9 migration",
                    cursor.moveToFirst(),
                )
            }

        // plant_instance_history must have 10 columns.
        var columnCount = 0
        migrated
            .query("PRAGMA table_info(plant_instance_history)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                }
            }
        assertEquals("plant_instance_history must have 10 columns", 10, columnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v8'")
            .use { cursor ->
                assertTrue("v8 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V8 Garden", cursor.getString(0))
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
    fun fixture_migrates_from_v9_to_v10_adding_activity_table_with_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("v9-fixture", 10, true)

        migrated
            .query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'activity'",
            ).use { cursor ->
                assertTrue(
                    "activity table must exist after v9 -> v10 migration",
                    cursor.moveToFirst(),
                )
            }

        // activity must have 19 columns.
        var columnCount = 0
        migrated
            .query("PRAGMA table_info(activity)")
            .use { cursor ->
                while (cursor.moveToNext()) {
                    columnCount += 1
                }
            }
        assertEquals("activity must have 19 columns", 19, columnCount)

        migrated
            .query("SELECT name FROM garden WHERE id = 'garden_test_v9'")
            .use { cursor ->
                assertTrue("v9 garden row must survive migration", cursor.moveToFirst())
                assertEquals("V9 Garden", cursor.getString(0))
            }

        migrated
            .query("SELECT name FROM growing_space WHERE id = 'growingspace_test_v9'")
            .use { cursor ->
                assertTrue("v9 growing_space row must survive migration", cursor.moveToFirst())
                assertEquals("V9 Bed", cursor.getString(0))
            }

        migrated.close()
    }
}
