package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import java.time.Instant
import java.time.ZoneOffset

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backFromAudioPlayerActivityButton)
        val header = findViewById<TextView>(R.id.header)
        val group = findViewById<TextView>(R.id.group)
        val lengthTime = findViewById<TextView>(R.id.lengthTime)
        val album = findViewById<TextView>(R.id.album)
        val albumName = findViewById<TextView>(R.id.albumName)
        val year = findViewById<TextView>(R.id.year)
        val yearNumber = findViewById<TextView>(R.id.yearNumber)
        val genreName = findViewById<TextView>(R.id.genreName)
        val countryName = findViewById<TextView>(R.id.countryName)
        val cover = findViewById<ImageView>(R.id.cover)

        backButton.setOnClickListener { finish() }

        val jsonString = intent.getStringExtra("track")
        val track = Gson().fromJson(jsonString, Track::class.java)

        if (track.collectionName.isNullOrEmpty()) {
            album.visibility = View.GONE
            albumName.visibility = View.GONE
        } else {
            albumName.text = track.collectionName
        }

        if (track.releaseDate == null) {
            year.visibility = View.GONE
            yearNumber.visibility = View.GONE
        } else {
            yearNumber.text = Instant.parse(track.releaseDate).atZone(ZoneOffset.UTC).year.toString()
        }

        header.text = track.trackName
        group.text = track.artistName
        lengthTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        genreName.text = track.primaryGenreName
        countryName.text = track.country

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(R.drawable.ic_audio_placeholder).into(cover)
    }
}