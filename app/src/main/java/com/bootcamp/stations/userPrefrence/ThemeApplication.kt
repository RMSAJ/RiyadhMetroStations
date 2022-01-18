package com.bootcamp.stations.userPrefrence

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bootcamp.stations.ThemeProvider

class ThemeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val theme = ThemeProvider(this).getThemeFromPreferences()
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}