package com.mytuin.gardenplanner.data.database.seed

/**
 * Seed content for first database creation.
 *
 * PHASE_0_PROJECT_FOUNDATION §11, §30 item 12.
 *
 * §11 permits a small number of example plants. This is deliberately
 * two rows and no more: enough to prove the mechanism iterates a
 * collection, no more than that. Real Plant Library content belongs
 * to a later step.
 *
 * Statements are SQL rather than PlantEntity instances because the
 * seeding hook (SeedCallback, A132=a) is Room's
 * RoomDatabase.Callback.onCreate, which is synchronous and has no
 * DAO access. Hand-written SQL matches what onCreate can execute
 * reliably. Drift between this SQL and PlantEntity is caught by
 * SeedDataTest, which reads back through the DAO.
 *
 * Status is 'active' (A134=a): these are normal reference rows, not
 * deprecated ones. The lifecycle vocabulary distinguishes current
 * from retired values; using 'deprecated' to signal "this is a seed"
 * would misuse that distinction.
 *
 * created_at and updated_at are 0. These rows are fixtures, not real
 * records; 0 is honest about there being no meaningful creation time.
 *
 * Future removal (A136=c): when the real Plant Library content lands,
 * a migration will delete these two rows. That is contingent on
 * verifying that no user data references them first — V1_DATABASE_SCHEMA
 * §8: knowledge records should be deprecated rather than deleted
 * *when historical recommendations or imported data still depend on
 * them*. If that verification cannot be made, the fallback is
 * deprecation (status = 'deprecated'), not deletion.
 */
object SeedData {
    /**
     * SQL statements to execute once, immediately after schema
     * creation. Order matters when foreign keys between seed rows
     * exist; today these are independent inserts into `plant`.
     */
    val insertStatements: List<String> =
        listOf(
            """
            INSERT INTO plant (
                id, canonical_name, scientific_name, genus, species, family,
                lifecycle, description, status, created_at, updated_at
            ) VALUES (
                'plant_example_0001',
                'Example Plant A',
                NULL, NULL, NULL, NULL, NULL,
                'Phase 0 seed row. Replaced by real Plant Library content in a later step.',
                'active',
                0,
                0
            )
            """.trimIndent(),
            """
            INSERT INTO plant (
                id, canonical_name, scientific_name, genus, species, family,
                lifecycle, description, status, created_at, updated_at
            ) VALUES (
                'plant_example_0002',
                'Example Plant B',
                NULL, NULL, NULL, NULL, NULL,
                'Phase 0 seed row. Replaced by real Plant Library content in a later step.',
                'active',
                0,
                0
            )
            """.trimIndent(),
        )
}
