package com.example.playlistmaker.settings.ui.activity

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
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.ui.view_model.utils.Resource
import com.example.playlistmaker.settings.ui.common.app.App
import com.example.playlistmaker.settings.ui.view_model.ThemeViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: ThemeViewModel

    private var currTheme: Boolean = false

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

        viewModel = ViewModelProvider(this, ThemeViewModel.getFactory(applicationContext)).get(ThemeViewModel::class.java)

        viewModel.observeTheme().observe(this) {
            if (it == null) {
                val systemDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
                viewModel.setTheme(systemDark)
            } else {
                currTheme = it
                if (switchButton.isChecked != currTheme) {
                    switchButton.isChecked = currTheme
                }
                (applicationContext as App).switchTheme(currTheme)
            }
        }

        switchButton.setOnCheckedChangeListener { switcher, checked ->
            if (checked != currTheme) {
                viewModel.setTheme(checked)
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