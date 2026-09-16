package com.mytuin.gardenplanner.domain.vocabulary

enum class RecordStatus(override val id: String) : VocabularyValue {
    DRAFT("draft"),
    ACTIVE("active"),
    INACTIVE("inactive"),
    DEPRECATED("deprecated"),
    ARCHIVED("archived"),
    ;

    companion object {
        fun fromId(id: String): RecordStatus? =
            entries.firstOrNull { it.id == id }
    }
}