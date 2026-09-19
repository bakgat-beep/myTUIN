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
    }
