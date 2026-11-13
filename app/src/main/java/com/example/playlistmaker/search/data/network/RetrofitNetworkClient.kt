package com.example.playlistmaker.search.data.network


import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class RetrofitNetworkClient(private val retrofit: Retrofit) : NetworkClient, KoinComponent {
    private val itunesService = retrofit.create(ITunesAPIService::class.java)
    override suspend fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = retrofit2.Response.success(itunesService.getTracks(dto.expression))
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}