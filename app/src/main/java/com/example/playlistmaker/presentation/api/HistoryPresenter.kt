package com.example.playlistmaker.presentation.api

import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track

abstract class HistoryPresenter {
    protected abstract val historyInteractor: SearchHistoryInteractor

    abstract fun isHistoryEmpty(): Boolean
    abstract fun addTrackToHistory(track: Track)
    abstract fun getTracksFromHistory(): List<Track>
    abstract fun clearHistory()
}