package com.example.playlistmaker.search.ui.view_model.utils

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Default<T> : Resource<T>()
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Empty<T> : Resource<T>()
    class Error<T>(message: String?) : Resource<T>(null, message)
}