package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val clearBtn = findViewById<ImageView>(R.id.search_clear_button)

        val textInput = findViewById<EditText>(R.id.text_inp)
        textInput.setText(text)

        val backBtn = findViewById<ImageView>(R.id.search_back_btn)
        backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        clearBtn.setOnClickListener {
            textInput.setText("")
        }

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = clearButtonVisibility(s)
                text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        textInput.addTextChangedListener(textWatcher)
    }



    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private var text: String = EMPTY_TEXT

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(SAVED_TEXT, EMPTY_TEXT)
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val EMPTY_TEXT = ""
    }

}