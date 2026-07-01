package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(private var tracks: List<Track>?) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks!!.get(position))
    }

    override fun getItemCount(): Int {
        return tracks?.size ?: 0
    }

    fun updateTracks(newTracks: List<Track>?) {
        this.tracks = newTracks
        notifyDataSetChanged()
    }
}