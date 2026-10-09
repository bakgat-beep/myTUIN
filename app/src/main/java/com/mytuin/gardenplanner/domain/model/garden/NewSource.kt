package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType

/**
 * User-settable fields for a new Source.
 *
 * The use case assigns id and createdAt. `status` defaults to
 * UNVERIFIED, matching a newly created source before any review.
 */
data class NewSource(
    val sourceType: SourceType,
    val status: SourceStatus = SourceStatus.UNVERIFIED,
    val title: String? = null,
    val authorOrOrganisation: String? = null,
    val publicationDate: KnownDate? = null,
    val accessDate: KnownDate? = null,
    val url: String? = null,
    val geographicScope: GeographicScope? = null,
    val hemisphere: Hemisphere? = null,
    val context: String? = null,
    val notes: String? = null,
)
