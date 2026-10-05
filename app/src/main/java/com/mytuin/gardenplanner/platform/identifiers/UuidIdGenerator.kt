package com.mytuin.gardenplanner.platform.identifiers

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import java.util.UUID
import javax.inject.Inject

class UuidIdGenerator
    @Inject
    constructor() : IdGenerator {
        override fun newGardenId(): String = "garden_${UUID.randomUUID()}"

        override fun newGrowingSpaceId(): String = "growingspace_${UUID.randomUUID()}"

        override fun newGrowingSpaceHistoryId(): String = "growingspacehistory_${UUID.randomUUID()}"

        override fun newAreaId(): String = "area_${UUID.randomUUID()}"

        override fun newSpatialObjectId(): String = "spatialobject_${UUID.randomUUID()}"

        override fun newPlantInstanceId(): String = "plantinstance_${UUID.randomUUID()}"

        override fun newPlantInstanceHistoryId(): String = "plantinstancehistory_${UUID.randomUUID()}"

        override fun newActivityId(): String = "activity_${UUID.randomUUID()}"

        override fun newObservationId(): String = "observation_${UUID.randomUUID()}"

        override fun newMeasurementId(): String = "measurement_${UUID.randomUUID()}"

        override fun newHarvestId(): String = "harvest_${UUID.randomUUID()}"

        override fun newHarvestLossId(): String = "harvestloss_${UUID.randomUUID()}"
    }
