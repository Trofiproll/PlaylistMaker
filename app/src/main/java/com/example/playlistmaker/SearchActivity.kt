package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {


    private lateinit var clearBtn: ImageView
    private lateinit var queryInput: EditText
    private lateinit var backBtn: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var updateBtn: Button


    private var text: String = EMPTY_TEXT


    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesServise = retrofit.create(ItunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        queryInput = findViewById(R.id.query_input)
        backBtn = findViewById(R.id.search_back_btn)
        recyclerView = findViewById(R.id.search_recyclerView)
        errorPlaceholder = findViewById(R.id.search_placeholder_error)
        nothingFoundPlaceholder = findViewById(R.id.search_placeholder_nothing_found)
        updateBtn = findViewById(R.id.search_update_button)
        clearBtn = findViewById(R.id.search_clear_button)

        queryInput.setText(text)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter


        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                itunesSearch()
                true
            }
            false
        }

        updateBtn.setOnClickListener { itunesSearch() }


        backBtn.setOnClickListener {
            finish()
        }

        clearBtn.setOnClickListener {
            queryInput.setText("")
            hideKeyboard()
            tracks.clear()
            adapter.notifyDataSetChanged()
        }

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.isVisible = !s.isNullOrEmpty()
                text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        queryInput.addTextChangedListener(textWatcher)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, text)
        Log.d("MY_TAG", "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(SAVED_TEXT, EMPTY_TEXT)
    }

    companion object {
        private const val SAVED_TEXT = "SAVED_TEXT"
        private const val EMPTY_TEXT = ""
    }

    private fun itunesSearch(){
        if(queryInput.text.isNotEmpty()){
            itunesServise.search(queryInput.text.toString()).enqueue(object :
                Callback<ItunesResponse>{
                override fun onResponse(call: Call<ItunesResponse>, response: Response<ItunesResponse>) {
                    Log.d("code", response.code().toString())
                    if (response.isSuccessful) {
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            //nothing found
                            nothingFoundPlaceholder.isVisible = true
                            errorPlaceholder.isVisible = false
                        } else {
                            //allright
                            nothingFoundPlaceholder.isVisible = false
                            errorPlaceholder.isVisible = false
                        }
                    } else {
                        //error
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        nothingFoundPlaceholder.isVisible = false
                        errorPlaceholder.isVisible = true
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    Log.d("code", t.message.toString())
                    tracks.clear()
                    adapter.notifyDataSetChanged()
                    val request = call.request()
                    nothingFoundPlaceholder.isVisible = false
                    errorPlaceholder.isVisible = true
                }

            })
        }
    }

    private fun hideKeyboard(){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }



}