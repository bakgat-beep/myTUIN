package com.mytuin.gardenplanner.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Migration test harness.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 8; DATA_MIGRATION_STRATEGY
 * §43–§46, §98.
 *
 * The three original fixtures now chain 1→3 (both auto-migrations).
 * A fourth exercises 2→3 directly. Together they cover every supported
 * source schema.
 *
 * validateDroppedTables = true: the helper fails if any table present
 * at the source schema is missing at the target without an explicit
 * drop. Our migrations only add tables, so this passes trivially and
 * guards against future accidental drops.
 */
@RunWith(AndroidJUnit4::class)
class GardenDatabaseMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        GardenDatabase::class.java,
    )

    @Test
    fun empty_database_fixture_migrates_from_v1_to_v3() {
        helper.createDatabase("empty-fixture", 1).close()

        val migrated = helper.runMigrationsAndValidate("empty-fixture", 3, true)

        listOf("growing_space", "growing_space_history").forEach { table ->
            migrated.query(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = '$table'"
            ).use { cursor ->
                assertTrue("$table must exist after migration", cursor.moveToFirst())
            }
        }
        migrated.close()
    }

    @Test
    fun minimal_garden_fixture_migrates_from_v1_to_v3_with_data_intact() {
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
                """.trimIndent()
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("minimal-garden-fixture", 3, true)

        migrated.query(
            "SELECT name FROM garden WHERE id = 'garden_test_minimal'"
        ).use { cursor ->
            assertTrue("v1 garden row must survive migration", cursor.moveToFirst())
            assertEquals("Minimal Garden", cursor.getString(0))
        }
        migrated.close()
    }

    @Test
    fun representative_garden_fixture_migrates_from_v1_to_v3_with_full_row_intact() {
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
                """.trimIndent()
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            "representative-garden-fixture", 3, true
        )

        migrated.query(
            """
            SELECT name, description, country_code, region, locality,
                   latitude, longitude, timezone, hemisphere
            FROM garden
            WHERE id = 'garden_test_representative'
            """.trimIndent()
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
    fun fixture_migrates_from_v2_to_v3_with_growing_space_intact() {
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
                """.trimIndent()
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
                """.trimIndent()
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate("v2-fixture", 3, true)

        migrated.query(
            """
            SELECT name, space_type, geometry_type, geometry_data
            FROM growing_space
            WHERE id = 'growingspace_test_v2'
            """.trimIndent()
        ).use { cursor ->
            assertTrue("v2 growing_space row must survive 2→3 migration", cursor.moveToFirst())
            assertEquals("V2 Bed", cursor.getString(0))
            assertEquals("raised_bed", cursor.getString(1))
            assertEquals("point", cursor.getString(2))
            assertEquals(
                """{"type":"Point","coordinates":[1.0,2.0]}""",
                cursor.getString(3),
            )
        }
        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'growing_space_history'"
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
        }
        migrated.close()
    }
}