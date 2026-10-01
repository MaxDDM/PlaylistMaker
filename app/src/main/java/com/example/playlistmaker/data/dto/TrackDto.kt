package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName
import kotlin.String

data class TrackDto(val trackId : String, val trackName: String, val artistName: String?, @SerializedName("trackTimeMillis") var trackTime: Long?, val artworkUrl100: String, val collectionName: String?, val releaseDate: String?, val primaryGenreName: String, val country: String, val previewUrl: String) { }