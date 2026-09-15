package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.domain.models.Track
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName, SimpleDateFormat("mm:ss", Locale.getDefault()).format( it.trackTime), it.artworkUrl100, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl) }
        } else {
            throw UnknownHostException()
        }
    }
}