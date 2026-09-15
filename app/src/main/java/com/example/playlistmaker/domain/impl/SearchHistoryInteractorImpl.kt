package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {
    private val savedTracks = repository.loadTracks()
    private val maxTracksCount = 10

    override fun isHistoryEmpty(): Boolean {
        return savedTracks.isEmpty()
    }

    override fun addTrack(track: Track) {
        if (savedTracks.find {it.trackId == track.trackId} != null) {
            savedTracks.removeAt(savedTracks.indexOfFirst { it.trackId == track.trackId })
        } else {
            if (savedTracks.size == maxTracksCount) {
                savedTracks.removeAt(9)
            }
        }
        savedTracks.add(0, track)

        repository.changeHistory(savedTracks)
    }

    override fun getTracks(): List<Track>{
        return savedTracks
    }

    override fun clearHistory() {
        savedTracks.clear()
        repository.clearHistory()
    }

}