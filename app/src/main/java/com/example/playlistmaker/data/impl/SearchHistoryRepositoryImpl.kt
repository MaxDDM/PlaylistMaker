package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(val sharedPrefs : SharedPreferences) : SearchHistoryRepository() {
    private val savedTracks = loadTracks()

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

        val jsonString = Gson().toJson(savedTracks)
        sharedPrefs.edit() {
            putString("saved_tracks", jsonString)
        }
    }

    override fun loadTracks(): MutableList<Track> {
        val trackListType = object : TypeToken<MutableList<Track>>() {}.type
        var tracks = Gson().fromJson<MutableList<Track>>(sharedPrefs.getString("saved_tracks", ""), trackListType)

        if (tracks == null) {
            tracks = mutableListOf()
        }
        return tracks
    }

    override fun getTracks(): MutableList<Track> {
        return savedTracks
    }

    override fun clearHistory() {
        savedTracks.clear()
        sharedPrefs.edit { remove("saved_tracks") }
    }
}