package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus

/**
 * User-settable fields for a new Plan.
 *
 * The use case assigns id, recordStatus (ACTIVE), createdAt and
 * updatedAt. `status` defaults to IDEA (a plan begins as an idea until
 * the user commits to it).
 */
data class NewPlan(
    val gardenId: String,
    val planType: ActivityType,
    val status: PlanningStatus = PlanningStatus.IDEA,
    val plannedStart: KnownDate? = null,
    val plannedEnd: KnownDate? = null,
    val quantity: Double? = null,
    val unit: ActivityQuantityUnit? = null,
    val priority: PlanningPriority? = null,
    val notes: String? = null,
)
