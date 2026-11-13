package com.example.playlistmaker.search.data.network


import com.example.playlistmaker.search.data.dto.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPIService {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") text: String) : Response
}