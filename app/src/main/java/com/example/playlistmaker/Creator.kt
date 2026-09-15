package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.domain.api.repositories.ThemeRepository
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(sharedPrefs : SharedPreferences) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPrefs)
    }

    private fun getThemeRepository(sharedPrefs: SharedPreferences) : ThemeRepository {
        return ThemeRepositoryImpl(sharedPrefs)
    }

    fun provideTracksInteractor() : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(sharedPrefs : SharedPreferences) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPrefs))
    }

    fun provideThemeInteractor(sharedPrefs: SharedPreferences) : ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(sharedPrefs))
    }
}