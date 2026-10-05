package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.repositories.ThemeRepository


class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {
    override fun getTheme(): Boolean? {
        return repository.getTheme()
    }

    override fun setTheme(isDark: Boolean) {
        repository.setTheme(isDark)
    }
}