package com.example.playlistmaker.main.domain.api.repositories

import com.example.playlistmaker.search.domain.models.Track

abstract class SearchHistoryRepository {
    abstract fun changeHistory(savedTracks : MutableList<Track>?)
    abstract fun loadTracks() : MutableList<Track>?
    abstract fun clearHistory()
}