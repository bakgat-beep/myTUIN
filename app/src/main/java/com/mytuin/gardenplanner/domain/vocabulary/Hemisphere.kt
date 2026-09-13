package com.mytuin.gardenplanner.domain.vocabulary

enum class Hemisphere(val id: String) {
    NORTHERN("northern"),
    SOUTHERN("southern"),
    EQUATORIAL("equatorial"),
    GLOBAL("global"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): Hemisphere? =
            entries.firstOrNull { it.id == id }
    }
}