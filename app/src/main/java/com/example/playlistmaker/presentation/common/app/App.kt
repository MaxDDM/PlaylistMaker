package com.example.playlistmaker.presentation.common.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator

class App : Application() {
    private var darkTheme = false
    private val themeInteractor by lazy { Creator.provideThemeInteractor(this) }

    override fun onCreate() {
        super.onCreate()

        val theme = themeInteractor.getTheme()
        if (!theme.isNullOrEmpty()) {
            if (theme == "dark") {
                switchTheme(true)
            } else {
                switchTheme(false)
            }
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