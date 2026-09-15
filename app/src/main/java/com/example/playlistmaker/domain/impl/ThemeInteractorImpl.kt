package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.domain.api.repositories.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {
    override fun getTheme(): String? {
        return repository.getTheme()
    }

    override fun setTheme(themeName: String) {
        repository.setTheme(themeName)
    }
}