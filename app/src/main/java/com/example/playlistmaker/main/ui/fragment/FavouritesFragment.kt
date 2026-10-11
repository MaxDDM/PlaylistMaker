package com.example.playlistmaker.main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.view_model.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {
    val favouritesViewModel: FavouritesViewModel by viewModel()
    companion object {
        fun newInstance() = FavouritesFragment().apply { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }
}