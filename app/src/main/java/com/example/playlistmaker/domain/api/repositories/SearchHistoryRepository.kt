package com.example.playlistmaker.domain.api.repositories

import com.example.playlistmaker.domain.models.Track

abstract class SearchHistoryRepository {
    abstract fun changeHistory(savedTracks : List<Track>)
    abstract fun loadTracks() : MutableList<Track>
    abstract fun clearHistory()
}