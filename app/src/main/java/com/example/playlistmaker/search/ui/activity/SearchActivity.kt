package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.common.adapter.track_adapter.TrackAdapter
import com.example.playlistmaker.main.ui.view_model.HistoryViewModel
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.common.adapter.track_adapter.TrackViewHolder
import com.example.playlistmaker.search.ui.view_model.TracksViewModel
import com.example.playlistmaker.search.ui.view_model.utils.Resource
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private var currentText = ""
    private lateinit var trackRecyclerView : RecyclerView
    private var trackAdapter = TrackAdapter(listOf()) { track ->
        if (clickDebounce()) {
            historyViewModel.addTrackToHistory(track)
            trackHistoryAdapter.notifyDataSetChanged()

            goToAudioPlayer(track)
        }
    }
    private var trackHistoryAdapter = TrackAdapter(listOf()) { track ->
        if (clickDebounce()) {
            goToAudioPlayer(track)
        }
    }
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var searchField: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var noMusicPic : ImageView
    private lateinit var noMusicText: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchRunnable = Runnable {
        tracksViewModel.searchTracks(searchField.text.toString())
    }
    private val tracksViewModel : TracksViewModel by viewModel()
    private val historyViewModel : HistoryViewModel by viewModel()

    private var isHistoryEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val clearButton = findViewById<ImageButton>(R.id.clearButton)
        searchField = findViewById(R.id.searchField)
        val hintMessage = findViewById<NestedScrollView>(R.id.hintMessage)
        val mainList = findViewById<LinearLayout>(R.id.mainList)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val backFromSearchActivityButton = findViewById<ImageButton>(R.id.backFromSearchActivityButton)
        val trackHistoryRecyclerView = findViewById<RecyclerView>(R.id.storyTrackList)
        progressBar = findViewById(R.id.progressBar)
        noMusicPic = findViewById(R.id.noMusicImg)
        noMusicText = findViewById(R.id.noMusicText)
        trackRecyclerView = findViewById(R.id.trackList)
        trackRecyclerView.adapter = trackAdapter
        trackHistoryRecyclerView.adapter = trackHistoryAdapter

        historyViewModel.observeHistory().observe(this) {
            if (!it.isNullOrEmpty()) {
                trackHistoryAdapter.updateTracks(it)
            }
        }

        historyViewModel.observeIsEmpty().observe(this) {
            isHistoryEmpty = it
        }

        tracksViewModel.observeTracks().observe(this) {
            when(it) {
                is Resource.Default -> default()
                is Resource.Loading -> prepareForSearch()
                is Resource.Success -> {showTracks(it.data)}
                is Resource.Empty -> {showNoMusicObj()}
                is Resource.Error -> {showNoInternetObj(it.message)}
            }
        }

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (!isHistoryEmpty) {
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }

        if (savedInstanceState != null) {
            searchField.setText(savedInstanceState.getString(SEARCH_FIELD_TEXT))
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty() && !isHistoryEmpty) {
                hintMessage.visibility = View.VISIBLE
                mainList.visibility = View.GONE
            } else {
                hintMessage.visibility = View.GONE
                mainList.visibility = View.VISIBLE
            }
        }

        backFromSearchActivityButton.setOnClickListener { finish() }

        clearButton.setOnClickListener {
            searchField.setText("")

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            searchField.clearFocus()

            trackAdapter.updateTracks(listOf())
        }

        clearHistoryButton.setOnClickListener {
            historyViewModel.clearHistory()
            hintMessage.visibility = View.GONE
            mainList.visibility = View.VISIBLE
        }

        val searchFieldTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                currentText = p0.toString()

                if (searchField.hasFocus()) {
                    if (!isHistoryEmpty) {
                        if (p0?.isEmpty() != false) {
                            hintMessage.visibility = View.VISIBLE
                            mainList.visibility = View.GONE
                            noMusicPic.visibility = View.GONE
                            noMusicText.visibility= View.GONE
                        } else {
                            hintMessage.visibility = View.GONE
                            mainList.visibility = View.VISIBLE
                        }
                    }
                }

                if (!p0.isNullOrEmpty()) {
                    searchDebounce()
                } else {
                    tracksViewModel.setDefault()
                }
            }

        }

        searchField.addTextChangedListener(searchFieldTextWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tracksViewModel.searchTracks(searchField.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_FIELD_TEXT, currentText)
    }

    private fun showNoInternetObj(text : String?) {
        progressBar.visibility = View.GONE
        trackRecyclerView.visibility = View.GONE

        val noInternetPic = findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = findViewById<TextView>(R.id.noInternetText)
        val noInternetButton = findViewById<Button>(R.id.noInternetButton)

        noInternetPic.visibility = View.VISIBLE
        noInternetText.visibility = View.VISIBLE
        noInternetButton.visibility = View.VISIBLE

        noInternetButton.setOnClickListener {
            trackRecyclerView.visibility = View.VISIBLE
            noInternetPic.visibility = View.GONE
            noInternetText.visibility = View.GONE
            noInternetButton.visibility = View.GONE
            if (!text.isNullOrEmpty()) {
                tracksViewModel.searchTracks(text)
            }
        }
    }

    private fun showNoMusicObj() {
        progressBar.visibility = View.GONE
        trackRecyclerView.visibility = View.GONE

        if (!currentText.isNullOrEmpty()) {
            noMusicPic.visibility = View.VISIBLE
            noMusicText.visibility = View.VISIBLE
        }
    }

    private fun showTracks(tracks: List<Track>?) {
        progressBar.visibility = View.GONE
        trackAdapter.updateTracks(tracks)
    }

    private fun goToAudioPlayer(track: Track) {
        val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)

        val jsonString = Gson().toJson(track)
        intent.putExtra("track", jsonString)

        startActivity(intent)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun default() {
        trackAdapter.updateTracks(listOf())

        val noMusicPic = findViewById<ImageView>(R.id.noMusicImg)
        val noMusicText = findViewById<TextView>(R.id.noMusicText)
        val noInternetPic = findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = findViewById<TextView>(R.id.noInternetText)
        val noInternetButton = findViewById<Button>(R.id.noInternetButton)

        noMusicPic.visibility = View.GONE
        noMusicText.visibility = View.GONE
        noInternetPic.visibility = View.GONE
        noInternetText.visibility = View.GONE
        noInternetButton.visibility = View.GONE
    }

    private fun prepareForSearch() {
        default()

        trackRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_FIELD_TEXT = "SEARCH_FIELD_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}