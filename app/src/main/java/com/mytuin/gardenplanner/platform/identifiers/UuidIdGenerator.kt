package com.mytuin.gardenplanner.platform.identifiers

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import java.util.UUID
import javax.inject.Inject

/**
 * Prefixed UUID v4 identifier generator (D1).
 *
 * Uses java.util.UUID, which is pure Java and carries no Android
 * dependency. The reason this lives in platform/ rather than
 * domain/ is organisational: domain/ holds interfaces, platform/
 * holds implementations that could be swapped.
 */
class UuidIdGenerator @Inject constructor() : IdGenerator {

    override fun newGardenId(): String = "garden_${UUID.randomUUID()}"
}