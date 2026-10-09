package com.example.playlistmaker.settings.di

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.interactors.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.repositories.ThemeRepository
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import android.content.Context
import com.example.playlistmaker.settings.data.StorageClient
import com.example.playlistmaker.settings.data.storage.PrefsStorageClient
import com.example.playlistmaker.settings.ui.view_model.ThemeViewModel
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named

val settingsModule = module {
    factory {
        ThemeInteractorImpl(get())
    } bind ThemeInteractor::class

    factory {
        ThemeRepositoryImpl(get<PrefsStorageClient<Boolean>>(named("themeStorage")))
    } bind ThemeRepository::class

    viewModel {
        ThemeViewModel(get())
    }

    factory (named("themeStorage")){
        PrefsStorageClient<Boolean>("theme", androidContext(), "theme", object : TypeToken<Boolean>() {}.type, get())
    } bind StorageClient::class
}