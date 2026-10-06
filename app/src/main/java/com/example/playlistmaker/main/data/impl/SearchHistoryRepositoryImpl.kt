package com.example.playlistmaker.main.data.impl

import android.content.Context
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.main.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.settings.data.storage.PrefsStorageClient
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class SearchHistoryRepositoryImpl(private val storage: PrefsStorageClient<MutableList<Track>>) : SearchHistoryRepository() {

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