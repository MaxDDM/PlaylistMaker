package com.example.playlistmaker.presentation.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.HistoryPresenter

class HistoryPresenterImpl(private val context : Context) : HistoryPresenter() {
    override val historyInteractor = Creator.provideSearchHistoryInteractor(context)

    override fun isHistoryEmpty(): Boolean {
        return historyInteractor.isHistoryEmpty()
    }

    override fun addTrackToHistory(track: Track) {
        historyInteractor.addTrack(track)
    }

    override fun getTracksFromHistory(): List<Track> {
        return historyInteractor.getTracks()
    }

    override fun clearHistory() {
        historyInteractor.clearHistory()
    }
}