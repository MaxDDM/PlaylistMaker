package com.example.playlistmaker.domain.api.repositories

import com.example.playlistmaker.domain.models.Track

abstract class SearchHistoryRepository {
    abstract fun isHistoryEmpty() : Boolean
    abstract fun addTrack(track : Track)
    protected abstract fun loadTracks() : MutableList<Track>
    abstract fun getTracks() : MutableList<Track>
    abstract fun clearHistory()
}