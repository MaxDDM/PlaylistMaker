package com.example.playlistmaker.search.domain.api.interactors

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    suspend fun searchTracks(expression: String): Flow<List<Track>>
}