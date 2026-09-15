package com.example.playlistmaker.presentation.api

import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.domain.models.Track

abstract class Presenter {
    protected abstract val historyInteractor: SearchHistoryInteractor
    protected abstract val tracksInteractor: TracksInteractor
    protected abstract val themeInteractor: ThemeInteractor

    abstract fun searchTracks(expression: String, consumer: TracksConsumer)
    abstract fun isHistoryEmpty(): Boolean
    abstract fun addTrackToHistory(track: Track)
    abstract fun getTracksFromHistory(): List<Track>
    abstract fun clearHistory()
    abstract fun setTheme(themeName: String)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
        fun onError(t: Throwable)
    }
}