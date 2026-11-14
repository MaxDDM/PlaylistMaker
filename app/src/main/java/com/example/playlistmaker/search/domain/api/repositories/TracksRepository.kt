package com.example.playlistmaker.search.domain.api.repositories

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface TracksRepository {
    suspend fun searchTracks(expression: String): Flow<List<Track>>
}