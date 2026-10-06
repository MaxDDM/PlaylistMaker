package com.example.playlistmaker.player.ui.activity


import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneOffset

class AudioPlayerActivity() : AppCompatActivity() {
    private var url = ""
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playButton = findViewById<ImageButton>(R.id.playButton)
        playButton.isEnabled = false

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
        val time = findViewById<TextView>(R.id.time)

        backButton.setOnClickListener { finish() }

        val jsonString = intent.getStringExtra("track")
        val gson: Gson = getKoin().get()
        val track = gson.fromJson(jsonString, Track::class.java)

        url = track.previewUrl

        viewModel.observeProgressTime().observe(this) {
            time.text = it
        }

        viewModel.observePlayerState().observe(this) {
            if(it == PlayerViewModel.STATE_PLAYING) {
                playButton.setImageResource(R.drawable.ic_pause)
            } else {
                playButton.setImageResource(R.drawable.ic_play)
            }

            playButton.isEnabled = it != PlayerViewModel.STATE_DEFAULT
        }

        playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        if (track.collectionName.isNullOrEmpty()) {
            album.visibility = View.GONE
            albumName.visibility = View.GONE
        } else {
            albumName.text = track.collectionName
        }

        if (track.releaseDate.isNullOrEmpty()) {
            year.visibility = View.GONE
            yearNumber.visibility = View.GONE
        } else {
            yearNumber.text = Instant.parse(track.releaseDate).atZone(ZoneOffset.UTC).year.toString()
        }

        header.text = track.trackName
        group.text = track.artistName
        lengthTime.text = track.trackTime
        genreName.text = track.primaryGenreName
        countryName.text = track.country

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(
            R.drawable.ic_audio_placeholder).into(cover)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}