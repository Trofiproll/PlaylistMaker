package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial


const val SHARED_PREFERENCES = "shared_preferences"
const val DARK_MODE_PREFS_KEY = "dark_mode_prefs_key"
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_MODE_PREFS_KEY,
            AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            sharedPrefs.edit()
                .putBoolean(DARK_MODE_PREFS_KEY, checked)
                .apply()
            (applicationContext as App).switchTheme(checked)
        }

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