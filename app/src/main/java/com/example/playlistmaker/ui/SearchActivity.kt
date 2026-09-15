package com.example.playlistmaker.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.inputmethod.InputMethodManager;
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.TracksPresenter
import com.example.playlistmaker.presentation.common.adapter.tracks_adapter.TrackAdapter
import com.example.playlistmaker.presentation.impl.HistoryPresenterImpl
import com.example.playlistmaker.presentation.impl.TracksPresenterImpl
import com.google.gson.Gson
import kotlin.jvm.java

class SearchActivity : AppCompatActivity() {

    private var currentText = ""
    private lateinit var trackRecyclerView : RecyclerView
    private var trackAdapter = TrackAdapter(listOf()) { track ->
        if (clickDebounce()) {
            historyPresenter.addTrackToHistory(track)
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
        prepareForSearch()
        getTracks(searchField.text.toString())
    }
    private val tracksPresenter = TracksPresenterImpl()
    private val historyPresenter by lazy { HistoryPresenterImpl(this) }

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

        trackHistoryAdapter.updateTracks(historyPresenter.getTracksFromHistory())

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (!historyPresenter.isHistoryEmpty()) {
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }

        if (savedInstanceState != null) {
            searchField.setText(savedInstanceState.getString(SEARCH_FIELD_TEXT))
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty() && !historyPresenter.isHistoryEmpty()) {
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
            historyPresenter.clearHistory()
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
                    if (!historyPresenter.isHistoryEmpty()) {
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
                }
            }

        }

        searchField.addTextChangedListener(searchFieldTextWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                prepareForSearch()

                getTracks(searchField.text.toString())
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

    private fun showNoInternetObj(text : String) {
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
            getTracks(text)
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

    private fun getTracks(expression : String) {
        val myConsumer = object : TracksPresenter.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {

                if (foundTracks.isEmpty()) {
                    handler.post {
                        showNoMusicObj()
                    }
                } else {
                    handler.post {
                        showTracks(foundTracks)
                    }
                }
            }

            override fun onError(t: Throwable) {
                handler.post {
                    showNoInternetObj(expression)
                }
            }

        }

        tracksPresenter.searchTracks(expression, myConsumer)
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

    private fun prepareForSearch() {
        trackAdapter.updateTracks(listOf())
        trackRecyclerView.visibility = View.VISIBLE

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