package com.sdcgroup.speedmeter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val INDICATOR_ENABLED_KEY = booleanPreferencesKey("indicatorEnabled")
val THEME_MODE_KEY = intPreferencesKey("themeMode")

enum class ThemeMode(val storageValue: Int) {
    SYSTEM(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun fromStorageValue(value: Int): ThemeMode =
            entries.firstOrNull { it.storageValue == value } ?: SYSTEM
    }
}

fun indicatorEnabledFlow(context: Context): Flow<Boolean> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> prefs[INDICATOR_ENABLED_KEY] ?: false }

suspend fun setIndicatorEnabled(context: Context, enabled: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[INDICATOR_ENABLED_KEY] = enabled
    }
}

fun themeModeFlow(context: Context): Flow<ThemeMode> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> ThemeMode.fromStorageValue(prefs[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.storageValue) }

suspend fun setThemeMode(context: Context, mode: ThemeMode) {
    context.dataStore.edit { prefs ->
        prefs[THEME_MODE_KEY] = mode.storageValue
    }
}
