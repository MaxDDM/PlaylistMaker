package com.example.playlistmaker.main.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel.Companion.STATE_DEFAULT
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.settings.ui.view_model.ThemeViewModel

class HistoryViewModel(context : Context) : ViewModel(){
    val historyInteractor = Creator.provideSearchHistoryInteractor(context)

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

    companion object {
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HistoryViewModel(context)
            }
        }
    }
}