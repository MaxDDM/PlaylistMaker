package com.example.playlistmaker

import android.os.Bundle
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
import android.widget.TextView
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var currentText = ""

    private lateinit var trackRecyclerView : RecyclerView
    private var trackAdapter = TrackAdapter(listOf())

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
        val searchField = findViewById<EditText>(R.id.searchField)
        trackRecyclerView = findViewById(R.id.trackList)
        trackRecyclerView.adapter = trackAdapter

        if (savedInstanceState != null) {
            searchField.setText(savedInstanceState.getString(SEARCH_FIELD_TEXT))
        }

        clearButton.setOnClickListener {
            searchField.setText("")

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            trackAdapter.updateTracks(listOf())
        }

        val searchFieldTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(p0)
                currentText = p0.toString()
            }

        }

        searchField.addTextChangedListener(searchFieldTextWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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

                getTracks(searchField.text.toString())
                true
            }
            false
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
        trackRecyclerView.visibility = View.GONE

        val noMusicPic = findViewById<ImageView>(R.id.noMusicImg)
        val noMusicText = findViewById<TextView>(R.id.noMusicText)

        noMusicPic.visibility = View.VISIBLE
        noMusicText.visibility= View.VISIBLE
    }

    private fun getTracks(text : String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val itunesService = retrofit.create(ITunesAPI::class.java)

        itunesService.getTracks(text).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(call : Call<TracksResponse>, response : Response<TracksResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackAdapter.updateTracks(response.body()?.results)
                    } else {
                        showNoMusicObj()
                    }
                } else {
                    showNoInternetObj(text)
                }
            }

            override fun onFailure(call: Call<TracksResponse?>?, t: Throwable?) {
                showNoInternetObj(text)
            }

        })
    }

    companion object {
        const val SEARCH_FIELD_TEXT = "SEARCH_FIELD_TEXT"
    }
}