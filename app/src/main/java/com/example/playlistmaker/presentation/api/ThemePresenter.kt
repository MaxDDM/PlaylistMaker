package com.example.playlistmaker.presentation.api

import com.example.playlistmaker.domain.api.interactors.ThemeInteractor

abstract class ThemePresenter {
    protected abstract val themeInteractor: ThemeInteractor

    abstract fun setTheme(themeName: String)
}