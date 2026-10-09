package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType

/**
 * Source.
 *
 * V1_DATABASE_SCHEMA §24. DATA_MODEL §21.
 *
 * Stable identifier format: "source_<uuid>".
 *
 * No foreign keys (SC1): Source is application-wide reference data,
 * not garden-scoped.
 *
 * Dates use the (epoch_day, time_of_day_millis) pair (SC6). Both
 * are nullable.
 *
 * No updated_at (SC8 (a)). No record_status (SC3 (a)).
 */
@Entity(
    tableName = "source",
    indices = [
        Index(value = ["source_type"]),
        Index(value = ["status"]),
    ],
)
data class SourceEntity(
    @PrimaryKey
    val id: String,
    val source_type: SourceType,
    val status: SourceStatus,
    val created_at: Long,
    val title: String? = null,
    val author_or_organisation: String? = null,
    val publication_date_epoch_day: Int? = null,
    val publication_date_time_of_day_millis: Int? = null,
    val access_date_epoch_day: Int? = null,
    val access_date_time_of_day_millis: Int? = null,
    val url: String? = null,
    val geographic_scope: GeographicScope? = null,
    val hemisphere: Hemisphere? = null,
    val context: String? = null,
    val notes: String? = null,
)
