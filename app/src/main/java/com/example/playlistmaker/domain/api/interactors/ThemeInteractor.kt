package com.example.playlistmaker.domain.api.interactors

interface ThemeInteractor {
    fun getTheme() : String?
    fun setTheme(themeName: String)
}