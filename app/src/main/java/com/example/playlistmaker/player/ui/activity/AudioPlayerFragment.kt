package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneOffset

class AudioPlayerFragment() : Fragment() {
    companion object {
        private const val ARGS = "track"

        fun createArgs(fact: String): Bundle =
            bundleOf(ARGS to fact)
    }
    private var url = ""
    private var jsonTrack: String? = ""
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(jsonTrack)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_audio_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<ImageButton>(R.id.playButton)
        playButton.isEnabled = false

        val backButton = view.findViewById<ImageButton>(R.id.backFromAudioPlayerActivityButton)
        val header = view.findViewById<TextView>(R.id.header)
        val group = view.findViewById<TextView>(R.id.group)
        val lengthTime = view.findViewById<TextView>(R.id.lengthTime)
        val album = view.findViewById<TextView>(R.id.album)
        val albumName = view.findViewById<TextView>(R.id.albumName)
        val year = view.findViewById<TextView>(R.id.year)
        val yearNumber = view.findViewById<TextView>(R.id.yearNumber)
        val genreName = view.findViewById<TextView>(R.id.genreName)
        val countryName = view.findViewById<TextView>(R.id.countryName)
        val cover = view.findViewById<ImageView>(R.id.cover)
        val time = view.findViewById<TextView>(R.id.time)

        backButton.setOnClickListener { findNavController().navigateUp() }

        jsonTrack = arguments?.getString(ARGS).orEmpty()

        viewModel.observeTrack().observe(viewLifecycleOwner) {
            url = it.previewUrl

            if (it.collectionName.isNullOrEmpty()) {
                album.visibility = View.GONE
                albumName.visibility = View.GONE
            } else {
                albumName.text = it.collectionName
            }

            if (it.releaseDate.isNullOrEmpty()) {
                year.visibility = View.GONE
                yearNumber.visibility = View.GONE
            } else {
                yearNumber.text = Instant.parse(it.releaseDate).atZone(ZoneOffset.UTC).year.toString()
            }

            header.text = it.trackName
            group.text = it.artistName
            lengthTime.text = it.trackTime
            genreName.text = it.primaryGenreName
            countryName.text = it.country

            Glide.with(this).load(it.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")).placeholder(
                R.drawable.ic_audio_placeholder).into(cover)
        }

        viewModel.observeProgressTime().observe(viewLifecycleOwner) {
            time.text = it
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
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
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}