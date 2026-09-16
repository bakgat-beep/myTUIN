package com.mytuin.gardenplanner.platform.files

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.domain.files.FileAccess
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException

/**
 * Tests for AndroidFileAccess.
 *
 * A116: androidTest, using file:// URIs pointing into the app's
 * cache directory. This exercises the file:// branch introduced by
 * A115=b without requiring FileProvider configuration.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 17.
 */
@RunWith(AndroidJUnit4::class)
class AndroidFileAccessTest {

    private lateinit var context: Context
    private lateinit var fileAccess: FileAccess
    private lateinit var tempFile: File

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        fileAccess = AndroidFileAccess(context)
        tempFile = File.createTempFile("fileaccess-test-", ".txt", context.cacheDir)
    }

    @After
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun write_then_read_round_trips_utf8_text() = runBlocking {
        val content = "Hello, myTUIN — garden data.\nLine two.\n"
        val uri = Uri.fromFile(tempFile).toString()

        fileAccess.writeText(uri, content)

        assertEquals(content, fileAccess.readText(uri))
    }

    @Test
    fun write_replaces_existing_contents() = runBlocking {
        val uri = Uri.fromFile(tempFile).toString()

        fileAccess.writeText(uri, "first")
        fileAccess.writeText(uri, "second")

        assertEquals("second", fileAccess.readText(uri))
    }

    @Test
    fun read_missing_file_throws_ioexception() = runBlocking {
        val missing = File(context.cacheDir, "does-not-exist-${System.nanoTime()}.txt")
        val uri = Uri.fromFile(missing).toString()

        try {
            fileAccess.readText(uri)
            error("Expected IOException")
        } catch (expected: IOException) {
            // expected
        }
    }

    @Test
    fun unsupported_scheme_throws_ioexception() = runBlocking {
        try {
            fileAccess.readText("android.resource://com.mytuin.gardenplanner/1")
            error("Expected IOException")
        } catch (expected: IOException) {
            // expected
        }
    }
}