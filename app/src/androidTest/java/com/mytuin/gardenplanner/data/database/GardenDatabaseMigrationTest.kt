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
 * §43–§46.
 *
 * Step 5c promotes the three fixtures from create-and-close to real
 * 1 → 2 migration tests. The migration is an @AutoMigration
 * (A60b=a); the helper resolves it from the @Database annotation's
 * autoMigrations field.
 *
 * validateDroppedTables = true: the helper cross-checks the two
 * schema JSONs and fails if any table present at v1 is absent at v2
 * without an explicit drop. Our migration only adds growing_space,
 * so this passes trivially and guards against future accidental drops.
 *
 * Reproducibility: DATA_MIGRATION_STRATEGY §120.
 */
@RunWith(AndroidJUnit4::class)
class GardenDatabaseMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        GardenDatabase::class.java,
    )

    @Test
    fun empty_database_fixture_migrates_from_v1_to_v2() {
        helper.createDatabase("empty-fixture", 1).close()

        val migrated = helper.runMigrationsAndValidate("empty-fixture", 2, true)

        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'growing_space'"
        ).use { cursor ->
            assertTrue("growing_space must exist after migration", cursor.moveToFirst())
        }
        migrated.close()
    }

    @Test
    fun minimal_garden_fixture_migrates_from_v1_to_v2_with_data_intact() {
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

        val migrated = helper.runMigrationsAndValidate("minimal-garden-fixture", 2, true)

        migrated.query(
            "SELECT name FROM garden WHERE id = 'garden_test_minimal'"
        ).use { cursor ->
            assertTrue("v1 garden row must survive migration", cursor.moveToFirst())
            assertEquals("Minimal Garden", cursor.getString(0))
        }
        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'growing_space'"
        ).use { cursor ->
            assertTrue("growing_space must exist after migration", cursor.moveToFirst())
        }
        migrated.close()
    }

    @Test
    fun representative_garden_fixture_migrates_from_v1_to_v2_with_full_row_intact() {
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
            "representative-garden-fixture", 2, true
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
        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'growing_space'"
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
        }
        migrated.close()
    }
}