package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.SourceEntity
import com.mytuin.gardenplanner.domain.model.garden.Source

fun SourceEntity.toDomain(): Source =
    Source(
        id = id,
        sourceType = source_type,
        status = status,
        createdAt = created_at,
        title = title,
        authorOrOrganisation = author_or_organisation,
        publicationDate =
            knownDateFromEntity(
                publication_date_epoch_day,
                publication_date_time_of_day_millis,
            ),
        accessDate =
            knownDateFromEntity(
                access_date_epoch_day,
                access_date_time_of_day_millis,
            ),
        url = url,
        geographicScope = geographic_scope,
        hemisphere = hemisphere,
        context = context,
        notes = notes,
    )

fun Source.toEntity(): SourceEntity =
    SourceEntity(
        id = id,
        source_type = sourceType,
        status = status,
        created_at = createdAt,
        title = title,
        author_or_organisation = authorOrOrganisation,
        publication_date_epoch_day = publicationDate?.date?.toEpochDay()?.toInt(),
        publication_date_time_of_day_millis = publicationDate.encodeTime(),
        access_date_epoch_day = accessDate?.date?.toEpochDay()?.toInt(),
        access_date_time_of_day_millis = accessDate.encodeTime(),
        url = url,
        geographic_scope = geographicScope,
        hemisphere = hemisphere,
        context = context,
        notes = notes,
    )
