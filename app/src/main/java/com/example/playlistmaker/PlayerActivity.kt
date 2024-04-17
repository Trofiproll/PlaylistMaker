package com.example.playlistmaker


import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageButton
    private lateinit var cover: ImageView
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var plusBtn: ImageButton
    private lateinit var playBtn: ImageButton
    private lateinit var likeBtn: ImageButton
    private lateinit var currentTime: TextView
    private lateinit var duration: TextView
    private lateinit var collection: TextView
    private lateinit var collectionGroup: Group
    private lateinit var date: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)

        backBtn = findViewById(R.id.player_backButton)
        cover = findViewById(R.id.player_cover)
        artistName = findViewById(R.id.player_artistName)
        trackName = findViewById(R.id.player_trackName)
        plusBtn = findViewById(R.id.plus_button)
        playBtn = findViewById(R.id.play_button)
        likeBtn = findViewById(R.id.like_button)
        currentTime = findViewById(R.id.cur_time)
        duration = findViewById(R.id.duration)
        collection = findViewById(R.id.collection)
        collectionGroup = findViewById(R.id.collection_group)
        date = findViewById(R.id.date)
        genre = findViewById(R.id.genre)
        country = findViewById(R.id.country)

        bind()

        backBtn.setOnClickListener {
            finish()
        }


    }



    fun bind(){
        Glide.with(applicationContext)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.musiclogo)
            .centerCrop()
            .transform(RoundedCorners(track.dpToPx(8f, this)))
            .into(cover)
        artistName.text = track.artistName
        trackName.text = track.trackName
        currentTime.text = "0:00"
        duration.text = track.getDuration()
        if(track.collectionName.isNotEmpty()) collection.text = track.collectionName
        else collectionGroup.isVisible = false
        date.text = track.releaseDate.substring(0,4)
        genre.text = track.primaryGenreName
        country.text = track.country

    }
}