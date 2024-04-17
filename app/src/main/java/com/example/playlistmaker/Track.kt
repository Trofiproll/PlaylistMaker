package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
    val trackId:Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getDuration() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}