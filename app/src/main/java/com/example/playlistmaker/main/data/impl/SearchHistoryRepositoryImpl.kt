package com.example.playlistmaker.main.data.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.main.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.settings.data.storage.PrefsStorageClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository() {
    private val storage = PrefsStorageClient<MutableList<Track>>("saved_tracks", context, "saved_tracks", object : TypeToken<MutableList<Track>>() {}.type)

    override fun changeHistory(savedTracks: MutableList<Track>?) {
        if (!savedTracks.isNullOrEmpty()) {
            storage.storeData(savedTracks)
        }
    }

    override fun loadTracks(): MutableList<Track>? {
        return storage.getData()
    }

    override fun clearHistory() {
        storage.removeData()
    }
}