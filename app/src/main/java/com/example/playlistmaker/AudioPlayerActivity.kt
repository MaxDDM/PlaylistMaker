package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private var mediaPlayer = MediaPlayer()
    private var mediaPlayerState = STATE_DEFAULT
    private lateinit var playButton: ImageButton
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var time: TextView
    private val TIMER_DELAY = 300L
    private val timerRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayerState == STATE_PLAYING) {
                time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)

                handler.postDelayed(this, TIMER_DELAY)
            }
        }
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

        playButton = findViewById(R.id.playButton)
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
        time = findViewById(R.id.time)

        backButton.setOnClickListener { finish() }

        val jsonString = intent.getStringExtra("track")
        val track = Gson().fromJson(jsonString, Track::class.java)

        preparePlayer(track.previewUrl)
        playButton.isEnabled = true

        playButton.setOnClickListener {
            playbackControl()
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
        lengthTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        genreName.text = track.primaryGenreName
        countryName.text = track.country

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(R.drawable.ic_audio_placeholder).into(cover)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(timerRunnable)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            mediaPlayerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            mediaPlayerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.ic_play)
            handler.removeCallbacks(timerRunnable)
            time.text = getString(R.string.default_timer)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        mediaPlayerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.ic_pause)
        handler.postDelayed(timerRunnable, TIMER_DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        mediaPlayerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.ic_play)
        handler.removeCallbacks(timerRunnable)
    }

    private fun playbackControl() {
        when(mediaPlayerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}