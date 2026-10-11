package com.example.playlistmaker.main.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel(private val historyInteractor: SearchHistoryInteractor) : ViewModel() {
    private val historyLiveData = MutableLiveData(getTracksFromHistory())
    fun observeHistory(): LiveData<MutableList<Track>?> = historyLiveData

    private val isEmptyLiveData = MutableLiveData(isHistoryEmpty())
    fun observeIsEmpty(): LiveData<Boolean> = isEmptyLiveData

    private fun isHistoryEmpty(): Boolean {
        return historyInteractor.isHistoryEmpty()
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrack(track)
        historyLiveData.postValue(getTracksFromHistory())
    }

    private fun getTracksFromHistory(): MutableList<Track>? {
        return historyInteractor.getTracks()
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        isEmptyLiveData.postValue(true)
    }
}