package com.application.places.source

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.application.data.source.local.LocalSourceTracker

/**
 * Implementation of the source tracker which stores information whether local cache is valid or not.
 */
class LocalSourceTrackerImpl(context: Context) : LocalSourceTracker {
    private val prefs: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override var isValid: Boolean
        get() = prefs.getBoolean(EXTRA_VALID_CACHE, false)
        set(value) {
            prefs.edit().putBoolean(EXTRA_VALID_CACHE, value).apply()
        }

    companion object {
        private const val EXTRA_VALID_CACHE = "IsCacheValid"
    }
}