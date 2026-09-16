package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator

/**
 * Deterministic id generator for tests.
 *
 * Produces "garden_<counter>" so tests can assert on identity without
 * depending on UUID randomness.
 */
class FakeIdGenerator(
    private val nextGardenIdValue: String = "garden_test_id_0001",
) : IdGenerator {

    override fun newGardenId(): String = nextGardenIdValue
}