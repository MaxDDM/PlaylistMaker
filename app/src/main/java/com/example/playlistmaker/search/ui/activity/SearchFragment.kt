package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.common.adapter.track_adapter.TrackAdapter
import com.example.playlistmaker.main.ui.view_model.HistoryViewModel
import com.example.playlistmaker.player.ui.activity.AudioPlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.TracksViewModel
import com.example.playlistmaker.search.ui.view_model.utils.Resource
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearButton = view.findViewById<ImageButton>(R.id.clearButton)
        searchField = view.findViewById(R.id.searchField)
        val hintMessage = view.findViewById<NestedScrollView>(R.id.hintMessage)
        val mainList = view.findViewById<LinearLayout>(R.id.mainList)
        val clearHistoryButton = view.findViewById<Button>(R.id.clearHistoryButton)
        val trackHistoryRecyclerView = view.findViewById<RecyclerView>(R.id.storyTrackList)
        progressBar = view.findViewById(R.id.progressBar)
        noMusicPic = view.findViewById(R.id.noMusicImg)
        noMusicText = view.findViewById(R.id.noMusicText)
        trackRecyclerView = view.findViewById(R.id.trackList)
        trackRecyclerView.adapter = trackAdapter
        trackHistoryRecyclerView.adapter = trackHistoryAdapter

        historyViewModel.observeHistory().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                trackHistoryAdapter.updateTracks(it)
            }
        }

        historyViewModel.observeIsEmpty().observe(viewLifecycleOwner) {
            isHistoryEmpty = it
        }

        tracksViewModel.observeTracks().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Default -> default(view)
                is Resource.Loading -> prepareForSearch(view)
                is Resource.Success -> {showTracks(it.data)}
                is Resource.Empty -> {showNoMusicObj()}
                is Resource.Error -> {showNoInternetObj(view, it.message)}
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

        clearButton.setOnClickListener {
            searchField.setText("")

            val view = activity?.currentFocus
            if (view != null) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun showNoInternetObj(view: View, text : String?) {
        progressBar.visibility = View.GONE
        trackRecyclerView.visibility = View.GONE

        val noInternetPic = view.findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = view.findViewById<TextView>(R.id.noInternetText)
        val noInternetButton = view.findViewById<Button>(R.id.noInternetButton)

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
        val jsonString = Gson().toJson(track)

        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment, AudioPlayerFragment.createArgs(jsonString))
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun default(view: View) {
        trackAdapter.updateTracks(listOf())

        val noMusicPic = view.findViewById<ImageView>(R.id.noMusicImg)
        val noMusicText = view.findViewById<TextView>(R.id.noMusicText)
        val noInternetPic = view.findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = view.findViewById<TextView>(R.id.noInternetText)
        val noInternetButton = view.findViewById<Button>(R.id.noInternetButton)

        noMusicPic.visibility = View.GONE
        noMusicText.visibility = View.GONE
        noInternetPic.visibility = View.GONE
        noInternetText.visibility = View.GONE
        noInternetButton.visibility = View.GONE
    }

    private fun prepareForSearch(view: View) {
        default(view)

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