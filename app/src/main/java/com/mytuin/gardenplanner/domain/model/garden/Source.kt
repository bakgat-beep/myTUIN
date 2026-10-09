package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType

/**
 * Source — domain model.
 *
 * V1_DATABASE_SCHEMA §24. DATA_MODEL §21.
 *
 * The origin of knowledge or imported information. Application-wide
 * reference data, not garden-scoped (SC1): no gardenId.
 *
 * `status` (SourceStatus) is the source's assessment lifecycle.
 * There is no separate RecordStatus (SC3 (a)): the status vocabulary
 * already provides the deprecation semantics knowledge records need.
 *
 * `publicationDate` and `accessDate` use KnownDate (SC6). Either may
 * be date-only or timestamped.
 *
 * `url` is free text (SC9): no scheme validation. The application
 * must remain useful offline.
 *
 * No `updatedAt` (SC8 (a)): §24 does not list one, and a status
 * change is a metadata edit to reference data, not a historical
 * event.
 *
 * No `sourceRelationship`: that describes how a specific knowledge
 * record relates to its source, and belongs on the referencing
 * record, not on Source itself (SC7).
 */
data class Source(
    val id: String,
    val sourceType: SourceType,
    val status: SourceStatus,
    val createdAt: Long,
    val title: String?,
    val authorOrOrganisation: String?,
    val publicationDate: KnownDate?,
    val accessDate: KnownDate?,
    val url: String?,
    val geographicScope: GeographicScope?,
    val hemisphere: Hemisphere?,
    val context: String?,
    val notes: String?,
)
