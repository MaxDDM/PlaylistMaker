package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backFromSettingsButton)
        val shareButton = findViewById<ImageButton>(R.id.shareButton)
        val supportButton = findViewById<ImageButton>(R.id.supportButton)
        val agreementButton = findViewById<ImageButton>(R.id.agreementButton)
        val switchButton = findViewById<Switch>(R.id.switchButton)

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchButton.isChecked = true
        } else {
            switchButton.isChecked = false
        }

        switchButton.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)

            val sharedPrefs = getSharedPreferences("themePreferences", MODE_PRIVATE)
            if(checked) {
                sharedPrefs.edit { putString("theme", "dark") }
            } else {
                sharedPrefs.edit { putString("theme", "notDark") }
            }
        }

        backButton.setOnClickListener { finish() }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareData))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.shareTitle)))
        }

        supportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectEmail))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.messageEmail))

            this.startActivity(intent)
        }

        agreementButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.agreementLink))

            this.startActivity(intent)
        }
    }
}