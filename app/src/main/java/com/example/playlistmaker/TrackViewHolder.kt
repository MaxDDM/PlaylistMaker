package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)) {
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImageView)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackAuthor: TextView = itemView.findViewById(R.id.trackAuthorName)
    private val trackButton: ImageButton = itemView.findViewById(R.id.trackButton)

    fun bind(model: Track) {
        Glide.with(itemView.context).load(model.artworkUrl100).placeholder(R.drawable.ic_placeholder).into(trackImage)
        trackName.text = model.trackName
        trackAuthor.text = model.artistName + " • " + model.trackTime
    }
}