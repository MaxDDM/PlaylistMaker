package com.example.playlistmaker.main.di

import com.example.playlistmaker.main.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.main.domain.api.interactors.SearchHistoryInteractor
import com.example.playlistmaker.main.domain.api.repositories.SearchHistoryRepository
import com.example.playlistmaker.main.domain.impl.SearchHistoryInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import android.content.Context
import com.example.playlistmaker.main.ui.view_model.HistoryViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.settings.data.StorageClient
import com.example.playlistmaker.settings.data.storage.PrefsStorageClient
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

val mainModule = module {
    factory {
        SearchHistoryInteractorImpl(get())
    } bind SearchHistoryInteractor::class

    factory {
        SearchHistoryRepositoryImpl(get<PrefsStorageClient<MutableList<Track>>>(named("searchHistoryStorage")))
    } bind SearchHistoryRepository::class

    viewModel {
        HistoryViewModel(get())
    }

    factory (named("searchHistoryStorage")) {
        PrefsStorageClient<MutableList<Track>>("search_history", androidContext(), "search_history", object : TypeToken<MutableList<Track>>() {}.type, get())
    } bind StorageClient::class
}