package com.example.playlistmaker.main.domain.impl

import com.example.playlistmaker.main.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.main.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    private val maxTracksCount = 10

    override fun isHistoryEmpty(): Boolean {
        return getTracks().isNullOrEmpty()
    }

    override fun addTrack(track: Track) {
        val savedTracks = getTracks()
        if (savedTracks != null) {
            if (savedTracks.find { it.trackId == track.trackId } != null) {
                savedTracks.removeAt(savedTracks.indexOfFirst { it.trackId == track.trackId })
            } else {
                if (savedTracks.size == maxTracksCount) {
                    savedTracks.removeAt(9)
                }
            }
            savedTracks.add(0, track)

            repository.changeHistory(savedTracks)
        } else {
            repository.changeHistory(mutableListOf(track))
        }


    }

    override fun getTracks(): MutableList<Track>? {
        return repository.loadTracks()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}