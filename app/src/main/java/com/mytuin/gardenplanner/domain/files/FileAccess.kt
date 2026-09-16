package com.mytuin.gardenplanner.domain.files

/**
 * Text file access, isolated from Android's document/file machinery.
 *
 * PHASE_0_PROJECT_FOUNDATION §20, §30 item 17:
 *   "The application should establish an abstraction for file access
 *    without implementing the complete import/export system during
 *    Phase 0. The platform layer should provide access to Android's
 *    document/file mechanisms."
 *
 * A109=a: interface in the domain, implementation in the platform
 * layer. Matches IdGenerator (domain/identifiers) and Clock
 * (domain/time). A future ImportGarden use case in domain/usecases/
 * can depend on this without importing the platform package.
 *
 * A110=a: text, UTF-8, two methods. Import/export is JSON — text.
 * Stream and byte variants can be added when a requirement needs
 * them (§33).
 *
 * A111=a: URIs are plain Strings. Nothing to swap them with in this
 * signature.
 *
 * A113=a: implementations may throw java.io.IOException. IOException
 * is a universal JVM type, not an infrastructure-specific exception
 * like SQLiteConstraintException, so it does not need wrapping at
 * this boundary. The eventual import use case will catch it and
 * decide what it means in context.
 *
 * A118: nothing calls this from production code yet. It exists
 * because §30 item 17 requires it as a Phase 0 deliverable.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface FileAccess {

    /**
     * Reads the full contents of [uri] as UTF-8 text.
     *
     * @throws java.io.IOException if the URI cannot be opened or the
     *         stream fails while reading.
     */
    suspend fun readText(uri: String): String

    /**
     * Writes [content] to [uri] as UTF-8 text, replacing any existing
     * contents.
     *
     * @throws java.io.IOException if the URI cannot be opened or the
     *         stream fails while writing.
     */
    suspend fun writeText(uri: String, content: String)
}