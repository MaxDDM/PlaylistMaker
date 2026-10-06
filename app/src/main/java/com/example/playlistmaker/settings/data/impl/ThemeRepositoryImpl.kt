package com.example.playlistmaker.settings.data.impl

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.example.playlistmaker.settings.data.storage.PrefsStorageClient
import com.example.playlistmaker.settings.domain.api.repositories.ThemeRepository

class ThemeRepositoryImpl(context: Context) : ThemeRepository {
    private val storage = PrefsStorageClient<Boolean>("themePreferences", context, "theme", object : TypeToken<Boolean>() {}.type)

    override fun getTheme(): Boolean? {
        return storage.getData()
    }

    override fun setTheme(isDark: Boolean) {
        storage.storeData(isDark)
    }
}