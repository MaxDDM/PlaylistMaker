package com.example.playlistmaker.search.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TracksViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {
    private val tracksLiveData: MutableLiveData<Resource<List<Track>>> = MutableLiveData(Resource.Default())
    fun observeTracks(): LiveData<Resource<List<Track>>> = tracksLiveData
    fun searchTracks(expression: String) {
        if (expression.isNotEmpty()) {
            tracksLiveData.postValue(Resource.Loading())

            viewModelScope.launch {
                tracksInteractor.searchTracks(expression)
                    .catch { exception ->
                        exception.message?.let { Log.e("TracksViewModel", it) }
                        tracksLiveData.postValue(Resource.Error(expression))
                        tracksLiveData.postValue(Resource.Error(expression))
                    }
                    .collect { value ->
                        if (value.isEmpty()) {
                            tracksLiveData.postValue(Resource.Empty())
                        } else {
                            tracksLiveData.postValue(Resource.Success(value))
                        }
                    }
            }
        }
    }

    fun setDefault() {
        tracksLiveData.postValue(Resource.Default())
    }
}
