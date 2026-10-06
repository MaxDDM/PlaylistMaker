package com.example.playlistmaker.settings.domain.api.repositories

interface ThemeRepository {
    fun getTheme() : Boolean?
    fun setTheme(isDark: Boolean)
}