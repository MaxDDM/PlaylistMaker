package com.example.playlistmaker.presentation.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import com.example.playlistmaker.Creator
import com.example.playlistmaker.presentation.api.ThemePresenter

class ThemePresenterImpl(private val context: Context) : ThemePresenter() {
    override val themeInteractor = Creator.provideThemeInteractor(context)

    override fun setTheme(themeName: String) {
        themeInteractor.setTheme(themeName)
    }
}