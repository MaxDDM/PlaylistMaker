package com.example.playlistmaker.settings.ui.common.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {
    private var darkTheme = false
    private val themeInteractor by lazy { Creator.provideThemeInteractor(this) }

    override fun onCreate() {
        super.onCreate()

        val isDark = themeInteractor.getTheme()
        if (isDark != null) {
            switchTheme(isDark)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}