package com.example.playlistmaker.presentation.api

import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.domain.models.Track

abstract class TracksPresenter {
    protected abstract val tracksInteractor: TracksInteractor
    abstract fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
        fun onError(t: Throwable)
    }
}