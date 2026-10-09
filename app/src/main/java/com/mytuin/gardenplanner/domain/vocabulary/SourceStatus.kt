package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SourceStatus.
 *
 * PROVENANCE_VOCABULARIES.md §7. All 7 values included.
 *
 * Describes the state of the source itself, not the reliability of
 * every claim drawn from it. A verified source can still contain
 * claim-level uncertainty.
 *
 * Distinct from RecordStatus: Source is reference data, and its
 * lifecycle is captured here, not by a general archival state
 * (SC3 (a)). Values like `outdated` and `superseded` provide the
 * deprecation semantics V1_DATABASE_SCHEMA §8 requires for knowledge
 * records.
 */
enum class SourceStatus(
    override val id: String,
) : VocabularyValue {
    UNVERIFIED("unverified"),
    VERIFIED("verified"),
    PARTIALLY_VERIFIED("partially_verified"),
    OUTDATED("outdated"),
    SUPERSEDED("superseded"),
    DISPUTED("disputed"),
    UNAVAILABLE("unavailable"),
    ;

    companion object {
        fun fromId(id: String): SourceStatus? = entries.firstOrNull { it.id == id }
    }
}
