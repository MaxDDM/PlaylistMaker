package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel.Companion.STATE_DEFAULT
import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.utils.Resource

class TracksViewModel : ViewModel(){
    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksViewModel()
            }
        }
    }
    private val tracksLiveData: MutableLiveData<Resource<List<Track>>> = MutableLiveData(Resource.Default())
    fun observeTracks(): LiveData<Resource<List<Track>>> = tracksLiveData
    private val tracksInteractor = Creator.provideTracksInteractor()

    fun searchTracks(expression: String) {
        if (expression.isNotEmpty()) {
            tracksLiveData.postValue(Resource.Loading())

            val myConsumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    if (foundTracks.isEmpty()) {
                        tracksLiveData.postValue(Resource.Empty())
                    } else {
                        tracksLiveData.postValue(Resource.Success(foundTracks))
                    }
                }

                override fun onError(t: Throwable) {
                    tracksLiveData.postValue(Resource.Error(expression))
                }
            }

            tracksInteractor.searchTracks(expression, myConsumer)
        }
    }

    fun setDefault() {
        tracksLiveData.postValue(Resource.Default())
    }
}
