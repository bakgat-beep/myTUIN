package com.mytuin.gardenplanner.domain.vocabulary

/**
 * AreaType.
 *
 * GARDEN_VOCABULARIES.md §3. Three values: garden, zone, section.
 *
 * A2: the three-value vocabulary is complete. An Area's specific
 * identity is carried by its name ("Vegetable Garden", "Orchard");
 * area_type is the coarse organisational classification. Not
 * user-extensible.
 */
enum class AreaType(
    override val id: String,
) : VocabularyValue {
    GARDEN("garden"),
    ZONE("zone"),
    SECTION("section"),
    ;

    companion object {
        fun fromId(id: String): AreaType? = entries.firstOrNull { it.id == id }
    }
}
