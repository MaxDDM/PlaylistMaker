package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.repositories.ThemeRepository

class ThemeRepositoryImpl(val sharedPrefs : SharedPreferences) : ThemeRepository {
    private val darkThemeName = "dark"
    private val notDarkThemeName = "notDark"

    override fun getTheme(): String? {
        return sharedPrefs.getString("theme", "")
    }

    override fun setTheme(themeName: String) {
        if (themeName != darkThemeName && themeName != notDarkThemeName) {
            return
        }
        sharedPrefs.edit { putString("theme", themeName) }
    }
}