package com.example.playlistmaker.domain.api.repositories

interface ThemeRepository {
    fun getTheme() : String?
    fun setTheme(themeName: String)
}