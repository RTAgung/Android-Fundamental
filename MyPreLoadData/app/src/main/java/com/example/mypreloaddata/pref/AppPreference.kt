package com.example.mypreloaddata.pref

import android.content.Context
import android.content.SharedPreferences

class AppPreference(context: Context){
    companion object {
        private const val PREFS_NAME = "MahasiswaPref"
        private const val APP_FIRST_RUN = "app_first_run"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var firstRun: Boolean
        get() = prefs.getBoolean(APP_FIRST_RUN, true)
    set(value) {
        prefs.edit().putBoolean(APP_FIRST_RUN, value as Boolean).apply()

    }
}