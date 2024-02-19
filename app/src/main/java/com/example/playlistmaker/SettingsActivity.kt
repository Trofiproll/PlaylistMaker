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
            startActivity(Intent(this, MainActivity::class.java))
        }

        val termsBtn = findViewById<FrameLayout>(R.id.settings_terms)
        termsBtn.setOnClickListener{
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        val supportBtn = findViewById<FrameLayout>(R.id.settings_support)
        supportBtn.setOnClickListener{
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("denispartola@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }

        val shareBtn = findViewById<FrameLayout>(R.id.settings_share)
        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            intent.type = "text/plain"
            startActivity(intent)
        }
    }
}