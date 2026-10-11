package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.utils.Resource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TracksViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {
    private val tracksLiveData: MutableLiveData<Resource<List<Track>>> = MutableLiveData(Resource.Default())
    fun observeTracks(): LiveData<Resource<List<Track>>> = tracksLiveData
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
