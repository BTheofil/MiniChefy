package hu.tb.minichefy.presentation.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("settings")

class DataStoreManager @Inject constructor(
    context: Context
) {
    private val settingsDataStore = context.dataStore

    suspend fun setNeverShowDialogInDetailsScreen() = settingsDataStore.edit { settings ->
        settings[recipeDetailsKey] = false
    }

    fun isDialogShouldShowInDetailsScreen() = settingsDataStore.data.map { preferences ->
            preferences[recipeDetailsKey] ?: true
        }

    private companion object DataStoreKey {
        val recipeDetailsKey = booleanPreferencesKey("recipe_details_inform_dialog")
    }
}