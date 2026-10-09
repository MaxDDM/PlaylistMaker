package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.interactors.TracksInteractor
import com.example.playlistmaker.search.domain.api.repositories.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.ui.view_model.TracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val searchModule = module {
    factory {
        TracksInteractorImpl(get(), get())
    } bind TracksInteractor::class

    factory {
        TracksRepositoryImpl(get())
    } bind TracksRepository::class

    factory {
        RetrofitNetworkClient(get())
    } bind NetworkClient::class

    viewModel {
        TracksViewModel(get())
    }

    factory {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}