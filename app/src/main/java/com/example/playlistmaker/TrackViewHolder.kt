package com.example.playlistmaker

import android.app.Application
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val cover = itemView.findViewById<ImageView>(R.id.cover)
    private val trackName = itemView.findViewById<TextView>(R.id.player_trackName)
    private val artistName = itemView.findViewById<TextView>(R.id.player_artistName)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackTime)

    fun bind(model: Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.getDuration()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.musiclogo)
            .centerCrop()
            .transform(RoundedCorners(model.dpToPx(2f, itemView.context)))
            .into(cover)
        artistName.requestLayout()
    }
}