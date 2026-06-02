package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchButton)
        val mediaButton = findViewById<Button>(R.id.mediaButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val mediaButtonClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            val intent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(intent)
        } }
        mediaButton.setOnClickListener(mediaButtonClickListener)


        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}