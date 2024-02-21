package edu.fullerton.bookishh

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MyPreferencesRepo private constructor(
    private val dataStore: DataStore<Preferences>
){
    companion object {
        private const val Preferences_Data_FILE_NAME = "settings"
        private val darkModeValKey = stringPreferencesKey("darkModeVal")
        private val languageKey = stringPreferencesKey("languageCheckboxVal")
        private val languageFrKey = stringPreferencesKey("languageCodeFrenchVal")
        private var INSTANCE: MyPreferencesRepo? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(Preferences_Data_FILE_NAME)
                }
                INSTANCE = MyPreferencesRepo(dataStore)
            }
        }

        fun get(): MyPreferencesRepo {
            return INSTANCE ?: throw IllegalStateException("My Instance is not been initialized")
        }
    }

    val darkModeVal = this.dataStore.data.map {
            prefs -> prefs[darkModeValKey] ?: "false"
    }.distinctUntilChanged()

    val languageCheckboxVal = this.dataStore.data.map {
            prefs -> prefs[languageKey] ?: "false"
    }.distinctUntilChanged()

    val languageCodeFrenchVal = this.dataStore.data.map {
            prefs -> prefs[languageFrKey] ?: "false"
    }.distinctUntilChanged()

    private suspend fun saveStringValue(key: Preferences.Key<String>, value: String) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveInput(value: String, index: Int) {
        val key: Preferences.Key<String> = when(index) {
            1 -> darkModeValKey
            2 -> languageKey
            3 -> languageFrKey
            else -> {
                throw NoSuchFieldException("Invalid input")
            }
        }
        this.saveStringValue(key, value)
    }
}