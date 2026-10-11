package com.example.playlistmaker.main.ui.di

import com.example.playlistmaker.main.ui.view_model.FavouritesViewModel
import com.example.playlistmaker.main.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        FavouritesViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}