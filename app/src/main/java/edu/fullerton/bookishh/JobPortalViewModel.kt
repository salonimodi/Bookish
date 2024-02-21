package edu.fullerton.bookishh

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class JobPortalViewModel : ViewModel() {
    private val pref = MyPreferencesRepo.get()

    @SuppressLint("RestrictedApi")
    fun saveInput(s: String, index: Int) {
        viewModelScope.launch {
            pref.saveInput(s, index)
            Log.v(PackageManagerCompat.LOG_TAG, "Done saving input")
        }
    }

    fun loadInputs(act: MainActivity) {
        viewModelScope.launch {
            pref.darkModeVal.collectLatest {
               act.darkModeVal = it.toBoolean()
            }
        }

        viewModelScope.launch {
            pref.languageCheckboxVal.collectLatest {
                act.languageCheckboxVal = it.toBoolean()
            }
        }

        viewModelScope.launch {
            pref.languageCodeFrenchVal.collectLatest {
                act.languageCodeFrenchVal = it.toBoolean()
            }
        }

    }


}