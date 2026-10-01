package com.example.playlistmaker.domain.api.interactors

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun isHistoryEmpty(): Boolean
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun clearHistory()
}