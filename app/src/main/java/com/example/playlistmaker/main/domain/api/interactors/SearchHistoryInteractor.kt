package com.example.playlistmaker.main.domain.api.interactors

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun isHistoryEmpty(): Boolean
    fun addTrack(track: Track)
    fun getTracks(): MutableList<Track>?
    fun clearHistory()
}