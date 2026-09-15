package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(val sharedPrefs : SharedPreferences) : SearchHistoryRepository() {
    override fun changeHistory(savedTracks: List<Track>) {
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

    override fun clearHistory() {
        sharedPrefs.edit { remove("saved_tracks") }
    }
}