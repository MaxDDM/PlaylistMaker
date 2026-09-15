package com.example.playlistmaker.presentation.impl

import android.content.SharedPreferences
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.Presenter

class PresenterImpl(private val sharedPrefs : SharedPreferences) : Presenter() {
    override val historyInteractor = Creator.provideSearchHistoryInteractor(sharedPrefs)
    override val tracksInteractor = Creator.provideTracksInteractor()
    override val themeInteractor = Creator.provideThemeInteractor(sharedPrefs)

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

    override fun isHistoryEmpty(): Boolean {
        return historyInteractor.isHistoryEmpty()
    }

    override fun addTrackToHistory(track: Track) {
        historyInteractor.addTrack(track)
    }

    override fun getTracksFromHistory(): List<Track> {
        return historyInteractor.getTracks()
    }

    override fun clearHistory() {
        historyInteractor.clearHistory()
    }

    override fun setTheme(themeName: String) {
        themeInteractor.setTheme(themeName)
    }
}