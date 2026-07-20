package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

class Track(val trackId : String, val trackName: String, val artistName: String?, @SerializedName("trackTimeMillis") var trackTime: Long?, val artworkUrl100: String) { }