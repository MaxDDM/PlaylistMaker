package com.example.playlistmaker.settings.data.impl

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.example.playlistmaker.settings.data.storage.PrefsStorageClient
import com.example.playlistmaker.settings.domain.api.repositories.ThemeRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class ThemeRepositoryImpl(private val storage: PrefsStorageClient<Boolean>) : ThemeRepository, KoinComponent {

    override fun getTheme(): Boolean? {
        return storage.getData()
    }

    override fun setTheme(isDark: Boolean) {
        storage.storeData(isDark)
    }
}