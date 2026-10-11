package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.playlistmaker.settings.domain.api.interactors.ThemeInteractor

class ThemeViewModel(private val themeInteractor: ThemeInteractor) : ViewModel() {
    private val themeLiveData: MutableLiveData<Boolean> = MutableLiveData(getTheme())
    fun observeTheme(): LiveData<Boolean> = themeLiveData

    private fun getTheme() : Boolean {
        val isDark = themeInteractor.getTheme()

        if (isDark != null) {
            return isDark
        } else {
            return false
        }
    }

    fun setTheme(isDark: Boolean) {
        themeLiveData.postValue(isDark)
        themeInteractor.setTheme(isDark)
    }
}