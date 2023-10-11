package com.jpmorgan.weather

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides an interface to access and modify app preferences.
 *
 * @property prefs SharedPreferences instance to access app's stored preferences.
 */
@Singleton
class MyPreference @Inject constructor(@ApplicationContext context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        // Constant for the preference name, ensuring it's consistent across app usage.
        private const val PREF_NAME = "weather_app_prefs"
    }
}
