package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<ImageView>(R.id.settings_back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        val termsBtn = findViewById<FrameLayout>(R.id.settings_terms)
        termsBtn.setOnClickListener{
            val url = Uri.parse(getString(R.string.practicum_offer_link))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        val supportBtn = findViewById<FrameLayout>(R.id.settings_support)
        supportBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
            startActivity(intent)
        }

        val shareBtn = findViewById<FrameLayout>(R.id.settings_share)
        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            intent.type = "text/plain"
            startActivity(intent)
        }
    }
}