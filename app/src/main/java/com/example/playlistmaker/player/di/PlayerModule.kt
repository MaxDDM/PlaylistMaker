package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    factory {
        MediaPlayer()
    }

    viewModel { (url: String) ->
        PlayerViewModel(url, get(), get())
    }
}