package com.example.playlistmaker


import androidx.core.content.edit
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPrefs : SharedPreferences) {
    private val savedTracks = loadTracks()

    fun isHistoryEmpty() : Boolean {
        return savedTracks.isEmpty()
    }

    fun addTrack(track : Track) {
        if (savedTracks.find {it.trackId == track.trackId} != null) {
            savedTracks.removeAt(savedTracks.indexOfFirst { it.trackId == track.trackId })
        } else {
            if (savedTracks.size == 10) {
                savedTracks.removeAt(9)
            }
        }
        savedTracks.add(0, track)

        val jsonString = Gson().toJson(savedTracks)
        sharedPrefs.edit() {
            putString("saved_tracks", jsonString)
        }
    }

    fun loadTracks() : MutableList<Track> {
        val trackListType = object : TypeToken<MutableList<Track>>() {}.type
        var tracks = Gson().fromJson<MutableList<Track>>(sharedPrefs.getString("saved_tracks", ""), trackListType)

        if (tracks == null) {
            tracks = mutableListOf()
        }
        return tracks
    }

    fun getTracks() : MutableList<Track> {
        return savedTracks
    }

    fun clearHistory() {
        savedTracks.clear()
        sharedPrefs.edit { remove("saved_tracks") }
    }
}