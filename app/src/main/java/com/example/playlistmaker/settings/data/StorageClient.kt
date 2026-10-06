package com.example.playlistmaker.settings.data

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
    fun removeData()
}