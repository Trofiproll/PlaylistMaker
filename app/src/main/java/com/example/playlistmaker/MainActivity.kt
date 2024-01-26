package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val mediathekaBtn = findViewById<Button>(R.id.mediatheka_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)



        searchBtn.setOnClickListener{
            startActivity(Intent(this, SearchActivity::class.java))
        }
        mediathekaBtn.setOnClickListener{
            startActivity(Intent(this, MediathekaActivity::class.java))
        }
        settingsBtn.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}