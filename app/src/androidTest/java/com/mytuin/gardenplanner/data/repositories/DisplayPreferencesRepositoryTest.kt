package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.domain.display.DisplayPreferences
import com.mytuin.gardenplanner.domain.display.ThemeMode
import com.mytuin.gardenplanner.domain.display.UnitSystem
import com.mytuin.gardenplanner.domain.repository.DisplayPreferencesRepository
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for DisplayPreferencesRepositoryImpl.
 *
 * A139=a: androidTest, matching the existing repository tests.
 *
 * Test isolation: each test gets its own DataStore instance backed by
 * a unique file, with its own CoroutineScope. The scope is cancelled
 * and the file deleted in tearDown. Without cancelling the scope, the
 * next test fails with "There are multiple DataStores active for the
 * same file" — a well-known DataStore testing pitfall.
 *
 * The production DataStore is provided by DataStoreModule via the
 * preferencesDataStore delegate at Application scope; these tests do
 * not exercise that delegate, they exercise the repository against a
 * directly constructed DataStore. The delegate wiring is covered
 * indirectly by Hilt's compile-time checks.
 */
@RunWith(AndroidJUnit4::class)
class DisplayPreferencesRepositoryTest {
    private lateinit var context: Context
    private lateinit var scope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: DisplayPreferencesRepository
    private lateinit var storeFile: File

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob() + Job())
        storeFile =
            context.preferencesDataStoreFile(
                "display-preferences-test-${System.nanoTime()}",
            )
        dataStore = PreferenceDataStoreFactory.create(scope = scope) { storeFile }
        repository = DisplayPreferencesRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
        storeFile.delete()
    }

    @Test
    fun preferences_default_when_nothing_has_been_written() =
        runBlocking {
            val prefs = repository.preferences.first()

            assertEquals(ThemeMode.SYSTEM, prefs.themeMode)
            assertEquals(UnitSystem.METRIC, prefs.unitSystem)
            assertEquals(DisplayPreferences.DEFAULT_LANGUAGE_CODE, prefs.languageCode)
        }

    @Test
    fun setThemeMode_round_trips() =
        runBlocking {
            repository.setThemeMode(ThemeMode.DARK)

            assertEquals(ThemeMode.DARK, repository.preferences.first().themeMode)
        }

    @Test
    fun setUnitSystem_round_trips() =
        runBlocking {
            repository.setUnitSystem(UnitSystem.IMPERIAL)

            assertEquals(UnitSystem.IMPERIAL, repository.preferences.first().unitSystem)
        }

    @Test
    fun setLanguageCode_round_trips() =
        runBlocking {
            repository.setLanguageCode("af")

            assertEquals("af", repository.preferences.first().languageCode)
        }

    @Test
    fun setting_one_preference_leaves_others_at_default() =
        runBlocking {
            repository.setThemeMode(ThemeMode.LIGHT)

            val prefs = repository.preferences.first()
            assertEquals(ThemeMode.LIGHT, prefs.themeMode)
            assertEquals(UnitSystem.METRIC, prefs.unitSystem)
            assertEquals(DisplayPreferences.DEFAULT_LANGUAGE_CODE, prefs.languageCode)
        }

    @Test
    fun setting_the_same_preference_twice_overwrites() =
        runBlocking {
            repository.setThemeMode(ThemeMode.LIGHT)
            repository.setThemeMode(ThemeMode.DARK)

            assertEquals(ThemeMode.DARK, repository.preferences.first().themeMode)
        }

    @Test
    fun all_three_preferences_persist_together() =
        runBlocking {
            repository.setThemeMode(ThemeMode.DARK)
            repository.setUnitSystem(UnitSystem.IMPERIAL)
            repository.setLanguageCode("af")

            val prefs = repository.preferences.first()
            assertEquals(ThemeMode.DARK, prefs.themeMode)
            assertEquals(UnitSystem.IMPERIAL, prefs.unitSystem)
            assertEquals("af", prefs.languageCode)
        }
}
