package com.example.playlistmaker.settings.domain.api.interactors

interface ThemeInteractor {
    fun getTheme() : Boolean?
    fun setTheme(isDark: Boolean)
}