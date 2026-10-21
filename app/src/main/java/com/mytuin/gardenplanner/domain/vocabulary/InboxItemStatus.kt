package com.mytuin.gardenplanner.domain.vocabulary

/**
 * InboxItemStatus.
 *
 * E5 (a). Three values.
 *
 * Derived from V1_DATABASE_SCHEMA §52 and §53. No vocabulary
 * document enumerates these formally.
 *
 * Mute is not a status (§53 distinguishes mute from dismiss). A
 * muted item is still `pending`; its suppression is represented by
 * the `muted_until` timestamp on the row.
 *
 * `dismissed` and `completed` are terminal. A dismissed item is not
 * "never show this again" (§53): muting plus time-based re-raise,
 * and future reoccurrences, are separate concerns.
 */
enum class InboxItemStatus(
    override val id: String,
) : VocabularyValue {
    PENDING("pending"),
    DISMISSED("dismissed"),
    COMPLETED("completed"),
    ;

    companion object {
        fun fromId(id: String): InboxItemStatus? = entries.firstOrNull { it.id == id }
    }
}
