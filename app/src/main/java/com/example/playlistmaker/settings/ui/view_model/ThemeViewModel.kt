package com.example.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.utils.Resource

class ThemeViewModel(context: Context) : ViewModel() {
    private val themeInteractor = Creator.provideThemeInteractor(context)

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

    companion object {
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ThemeViewModel(context)
            }
        }
    }
}