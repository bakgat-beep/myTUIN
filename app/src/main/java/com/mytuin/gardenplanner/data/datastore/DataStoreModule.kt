package com.mytuin.gardenplanner.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The display DataStore file name.
 *
 * Distinct from Room's garden.db: display preferences live in their
 * own file, and DEC-042 keeps them out of the garden schema and out
 * of exports.
 */
private const val DISPLAY_PREFERENCES_FILE = "display_preferences"

/**
 * Top-level delegate.
 *
 * The preferencesDataStore delegate ensures one DataStore instance
 * per file for the process lifetime. Constructing a second instance
 * against the same file throws "There are multiple DataStores active
 * for the same file". Using the delegate avoids that class of bug
 * and matches the official guidance.
 */
private val Context.displayPreferencesDataStore: DataStore<Preferences> by
    preferencesDataStore(name = DISPLAY_PREFERENCES_FILE)

/**
 * Provides the display DataStore.
 *
 * Scoped @Singleton so all repository instances share one underlying
 * DataStore. Without the scope, Hilt would construct a new delegate
 * per injection and the multiple-instances error would follow.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDisplayPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.displayPreferencesDataStore
}
