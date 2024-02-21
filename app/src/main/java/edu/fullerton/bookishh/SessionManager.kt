package edu.fullerton.bookishh

import android.content.Context
import android.content.SharedPreferences
class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = prefs.edit()
    companion object {
        const val USER_TOKEN = "user_token"
        const val LOGGED_IN = "logged_in"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        editor.putString(USER_TOKEN, token)
    }

    fun setLogggedInStatus(isLoggedIn: Boolean) {
        editor.putBoolean(LOGGED_IN, isLoggedIn).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun getLoggedInStatus(): Boolean {
        return prefs.getBoolean(LOGGED_IN, false)
    }
    fun clearSession() {
        editor.clear().apply()
    }
}