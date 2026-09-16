package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator

/**
 * Deterministic id generator for tests.
 */
class FakeIdGenerator(
    private val nextGardenIdValue: String = "garden_test_id_0001",
    private val nextGrowingSpaceIdValue: String = "growingspace_test_id_0001",
) : IdGenerator {

    override fun newGardenId(): String = nextGardenIdValue

    override fun newGrowingSpaceId(): String = nextGrowingSpaceIdValue
}