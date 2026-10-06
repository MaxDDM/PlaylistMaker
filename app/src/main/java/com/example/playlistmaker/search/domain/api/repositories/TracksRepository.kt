package com.example.playlistmaker.search.domain.api.repositories

import com.example.playlistmaker.search.domain.models.Track


interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}