package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.main.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.main.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.main.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.settings.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.main.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.api.repositories.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.repositories.ThemeRepository
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl

object Creator {
    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient()
        )
    }

    private fun getSearchHistoryRepository(context: Context) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    private fun getThemeRepository(context: Context) : ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    fun provideTracksInteractor() : TracksInteractor {
        return TracksInteractorImpl(
            getTracksRepository()
        )
    }

    fun provideSearchHistoryInteractor(context: Context) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(
            getSearchHistoryRepository(context)
        )
    }

    fun provideThemeInteractor(context: Context) : ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }
}