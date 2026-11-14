package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.api.repositories.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor, KoinComponent {
    override suspend fun searchTracks(expression: String): Flow<List<Track>> {
        return repository.searchTracks(expression)
    }
}