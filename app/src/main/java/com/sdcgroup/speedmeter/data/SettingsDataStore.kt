package com.sdcgroup.speedmeter.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val INDICATOR_ENABLED_KEY = booleanPreferencesKey("indicatorEnabled")
val THEME_MODE_KEY = intPreferencesKey("themeMode")
val SPEED_UNIT_KEY = intPreferencesKey("speedUnit")
val DATA_UNIT_KEY = intPreferencesKey("dataUnit")
val ICON_SPEED_TYPE_KEY = intPreferencesKey("iconSpeedType")
val START_ON_BOOT_KEY = booleanPreferencesKey("startOnBoot")
val QUIT_REQUESTED_KEY = booleanPreferencesKey("quitRequested")
val DAILY_USAGE_DATE_KEY = stringPreferencesKey("dailyUsageDate")
val DAILY_DATA_BASELINE_KEY = longPreferencesKey("dailyDataBaseline")
val DAILY_WIFI_BASELINE_KEY = longPreferencesKey("dailyWifiBaseline")

enum class ThemeMode(val storageValue: Int) {
    SYSTEM(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun fromStorageValue(value: Int): ThemeMode =
            entries.firstOrNull { it.storageValue == value } ?: SYSTEM
    }
}

enum class SpeedUnit(val storageValue: Int) {
    BYTES_PER_SECOND(0),
    BITS_PER_SECOND(1);

    companion object {
        fun fromStorageValue(value: Int): SpeedUnit =
            entries.firstOrNull { it.storageValue == value } ?: BYTES_PER_SECOND
    }
}

enum class DataUnit(val storageValue: Int) {
    BITS_DECIMAL(0),
    BYTES_DECIMAL(1),
    BYTES_BINARY(2);

    companion object {
        fun fromStorageValue(value: Int): DataUnit =
            entries.firstOrNull { it.storageValue == value } ?: BYTES_BINARY
    }
}

enum class IconSpeedType(val storageValue: Int) {
    TOTAL(0),
    DOWNLOAD(1),
    UPLOAD(2);

    companion object {
        fun fromStorageValue(value: Int): IconSpeedType =
            entries.firstOrNull { it.storageValue == value } ?: TOTAL
    }
}

data class DailyUsageBaseline(
    val date: String,
    val dataBaseline: Long,
    val wifiBaseline: Long
)

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

fun speedUnitFlow(context: Context): Flow<SpeedUnit> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> SpeedUnit.fromStorageValue(prefs[SPEED_UNIT_KEY] ?: SpeedUnit.BYTES_PER_SECOND.storageValue) }

suspend fun setSpeedUnit(context: Context, unit: SpeedUnit) {
    context.dataStore.edit { prefs ->
        prefs[SPEED_UNIT_KEY] = unit.storageValue
    }
}

fun dataUnitFlow(context: Context): Flow<DataUnit> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> DataUnit.fromStorageValue(prefs[DATA_UNIT_KEY] ?: DataUnit.BYTES_BINARY.storageValue) }

suspend fun setDataUnit(context: Context, unit: DataUnit) {
    context.dataStore.edit { prefs ->
        prefs[DATA_UNIT_KEY] = unit.storageValue
    }
}

fun iconSpeedTypeFlow(context: Context): Flow<IconSpeedType> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> IconSpeedType.fromStorageValue(prefs[ICON_SPEED_TYPE_KEY] ?: IconSpeedType.TOTAL.storageValue) }

suspend fun setIconSpeedType(context: Context, type: IconSpeedType) {
    context.dataStore.edit { prefs ->
        prefs[ICON_SPEED_TYPE_KEY] = type.storageValue
    }
}

fun startOnBootFlow(context: Context): Flow<Boolean> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> prefs[START_ON_BOOT_KEY] ?: false }

suspend fun setStartOnBoot(context: Context, enabled: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[START_ON_BOOT_KEY] = enabled
    }
}

fun quitRequestedFlow(context: Context): Flow<Boolean> =
    context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs -> prefs[QUIT_REQUESTED_KEY] ?: false }

suspend fun setQuitRequested(context: Context, quit: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[QUIT_REQUESTED_KEY] = quit
    }
}

suspend fun getDailyUsageBaseline(context: Context): DailyUsageBaseline {
    val prefs = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .first()
    return DailyUsageBaseline(
        date = prefs[DAILY_USAGE_DATE_KEY] ?: "",
        dataBaseline = prefs[DAILY_DATA_BASELINE_KEY] ?: 0L,
        wifiBaseline = prefs[DAILY_WIFI_BASELINE_KEY] ?: 0L
    )
}

suspend fun saveDailyUsageBaseline(context: Context, date: String, dataBaseline: Long, wifiBaseline: Long) {
    context.dataStore.edit { prefs ->
        prefs[DAILY_USAGE_DATE_KEY] = date
        prefs[DAILY_DATA_BASELINE_KEY] = dataBaseline
        prefs[DAILY_WIFI_BASELINE_KEY] = wifiBaseline
    }
}
