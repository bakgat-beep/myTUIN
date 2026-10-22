package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SupportType.
 *
 * PLANT_VOCABULARIES §41 `support_type`. §23 names illustrative
 * examples (trellis, stake, cage, fence, frame) without an
 * authoritative list. The value set below extends those with a
 * small number of common forms.
 *
 * Multi-value: a plant may benefit from more than one support type.
 * SupportRequirement (§23) is separate: it says whether support is
 * needed; SupportType says what kind.
 */
enum class SupportType(
    override val id: String,
) : VocabularyValue {
    TRELLIS("trellis"),
    STAKE("stake"),
    CAGE("cage"),
    FENCE("fence"),
    FRAME("frame"),
    ARCH("arch"),
    OBELISK("obelisk"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SupportType? = entries.firstOrNull { it.id == id }
    }
}
