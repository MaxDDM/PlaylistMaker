package com.example.playlistmaker.search.data

interface NetworkClient {
    suspend fun doRequest(dto: Any): com.example.playlistmaker.search.data.dto.Response
}