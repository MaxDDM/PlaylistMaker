package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {
    override fun isHistoryEmpty(): Boolean {
        return repository.isHistoryEmpty()
    }

    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun getTracks(): List<Track>{
        return repository.getTracks()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}