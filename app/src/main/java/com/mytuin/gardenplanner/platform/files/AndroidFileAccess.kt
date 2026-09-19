package com.mytuin.gardenplanner.platform.files

import android.content.Context
import android.net.Uri
import com.mytuin.gardenplanner.domain.files.FileAccess
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of FileAccess.
 *
 * A115=b: supports content:// (SAF output) and file:// (used by
 * tests, and by any future code writing into the app's cache or
 * files directories). android.resource:// is not supported — nothing
 * needs to read resources this way, and adding it would be
 * speculative.
 *
 * A114=a: dispatches to Dispatchers.IO. Interface methods are
 * suspend; callers do not choose a dispatcher.
 *
 * UTF-8 is hard-coded rather than exposed as a parameter. If a future
 * requirement needs a different encoding, that is a different method
 * on the interface, not a hidden default on this one.
 */
class AndroidFileAccess
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : FileAccess {
        override suspend fun readText(uri: String): String =
            withContext(Dispatchers.IO) {
                val parsed = Uri.parse(uri)
                val stream =
                    when (parsed.scheme) {
                        "content" -> context.contentResolver.openInputStream(parsed)
                        "file" -> parsed.path?.let { FileInputStream(File(it)) }
                        else -> null
                    } ?: throw IOException("Cannot open URI for reading: $uri")

                stream.use { it.readBytes().toString(Charsets.UTF_8) }
            }

        override suspend fun writeText(
            uri: String,
            content: String,
        ) = withContext(Dispatchers.IO) {
            val parsed = Uri.parse(uri)
            val stream =
                when (parsed.scheme) {
                    "content" -> context.contentResolver.openOutputStream(parsed)
                    "file" -> parsed.path?.let { FileOutputStream(File(it)) }
                    else -> null
                } ?: throw IOException("Cannot open URI for writing: $uri")

            stream.use { it.write(content.toByteArray(Charsets.UTF_8)) }
        }
    }
