package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator

class FakeIdGenerator(
    private val nextGardenIdValue: String = "garden_test_id_0001",
    private val nextGrowingSpaceIdValue: String = "growingspace_test_id_0001",
    private val nextGrowingSpaceHistoryIdValue: String = "growingspacehistory_test_id_0001",
    private val nextAreaIdValue: String = "area_test_id_0001",
) : IdGenerator {
    override fun newGardenId(): String = nextGardenIdValue

    override fun newGrowingSpaceId(): String = nextGrowingSpaceIdValue

    override fun newGrowingSpaceHistoryId(): String = nextGrowingSpaceHistoryIdValue

    override fun newAreaId(): String = nextAreaIdValue
}
