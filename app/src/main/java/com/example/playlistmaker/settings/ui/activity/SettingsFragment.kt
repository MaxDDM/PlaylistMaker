package com.example.playlistmaker.settings.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.common.app.App
import com.example.playlistmaker.settings.ui.view_model.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel: ThemeViewModel by viewModel()

    private var currTheme: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shareButton = view.findViewById<ImageButton>(R.id.shareButton)
        val supportButton = view.findViewById<ImageButton>(R.id.supportButton)
        val agreementButton = view.findViewById<ImageButton>(R.id.agreementButton)
        val switchButton = view.findViewById<Switch>(R.id.switchButton)

        viewModel.observeTheme().observe(viewLifecycleOwner) {
            if (it == null) {
                val systemDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
                viewModel.setTheme(systemDark)
            } else {
                currTheme = it
                if (switchButton.isChecked != currTheme) {
                    switchButton.isChecked = currTheme
                }
                (requireContext().applicationContext as App).switchTheme(currTheme)
            }
        }

        switchButton.setOnCheckedChangeListener { switcher, checked ->
            if (checked != currTheme) {
                viewModel.setTheme(checked)
            }
        }

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