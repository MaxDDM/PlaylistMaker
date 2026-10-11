package com.example.playlistmaker.di_common

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import org.koin.dsl.module
import java.util.concurrent.Executors

val commonModule = module {
    factory {
        Gson()
    }

    factory {
        Executors.newCachedThreadPool()
    }

    factory {
        Handler(Looper.getMainLooper())
    }
}