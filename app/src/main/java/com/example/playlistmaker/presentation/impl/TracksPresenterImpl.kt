package com.example.playlistmaker.presentation.impl

import android.content.SharedPreferences
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.TracksPresenter

class TracksPresenterImpl : TracksPresenter() {
    override val tracksInteractor = Creator.provideTracksInteractor()

    override fun searchTracks(expression: String, consumer: TracksConsumer) {
        var res = listOf<Track>()

        val myConsumer = object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                consumer.consume(foundTracks)
            }

            override fun onError(t: Throwable) {
                consumer.onError(t)
            }
        }

        tracksInteractor.searchTracks(expression, myConsumer)
    }
}