package com.mytuin.gardenplanner.domain.vocabulary

/**
 * InboxItemType.
 *
 * E4 (a). Derived from the examples listed in V1_DATABASE_SCHEMA
 * §52: "action; warning; observation request; information request;
 * experiment; knowledge."
 *
 * No vocabulary document enumerates these. The values are
 * transcribed from the schema prose. If a future document authors
 * this classification formally, this enum may need to be reconciled
 * with it.
 */
enum class InboxItemType(
    override val id: String,
) : VocabularyValue {
    ACTION("action"),
    WARNING("warning"),
    OBSERVATION_REQUEST("observation_request"),
    INFORMATION_REQUEST("information_request"),
    EXPERIMENT("experiment"),
    KNOWLEDGE("knowledge"),
    ;

    companion object {
        fun fromId(id: String): InboxItemType? = entries.firstOrNull { it.id == id }
    }
}
