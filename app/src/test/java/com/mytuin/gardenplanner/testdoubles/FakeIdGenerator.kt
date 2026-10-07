package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator

class FakeIdGenerator(
    private val nextGardenIdValue: String = "garden_test_id_0001",
    private val nextGrowingSpaceIdValue: String = "growingspace_test_id_0001",
    private val nextGrowingSpaceHistoryIdValue: String = "growingspacehistory_test_id_0001",
    private val nextAreaIdValue: String = "area_test_id_0001",
    private val nextSpatialObjectIdValue: String = "spatialobject_test_id_0001",
    private val nextPlantInstanceIdValue: String = "plantinstance_test_id_0001",
    private val nextPlantInstanceHistoryIdValue: String = "plantinstancehistory_test_id_0001",
    private val nextActivityIdValue: String = "activity_test_id_0001",
    private val nextObservationIdValue: String = "observation_test_id_0001",
    private val nextMeasurementIdValue: String = "measurement_test_id_0001",
    private val nextHarvestIdValue: String = "harvest_test_id_0001",
    private val nextHarvestLossIdValue: String = "harvestloss_test_id_0001",
    private val nextProblemIdValue: String = "problem_test_id_0001",
) : IdGenerator {
    override fun newGardenId(): String = nextGardenIdValue

    override fun newGrowingSpaceId(): String = nextGrowingSpaceIdValue

    override fun newGrowingSpaceHistoryId(): String = nextGrowingSpaceHistoryIdValue

    override fun newAreaId(): String = nextAreaIdValue

    override fun newSpatialObjectId(): String = nextSpatialObjectIdValue

    override fun newPlantInstanceId(): String = nextPlantInstanceIdValue

    override fun newPlantInstanceHistoryId(): String = nextPlantInstanceHistoryIdValue

    override fun newActivityId(): String = nextActivityIdValue

    override fun newObservationId(): String = nextObservationIdValue

    override fun newMeasurementId(): String = nextMeasurementIdValue

    override fun newHarvestId(): String = nextHarvestIdValue

    override fun newHarvestLossId(): String = nextHarvestLossIdValue

    override fun newProblemId(): String = nextProblemIdValue
}
