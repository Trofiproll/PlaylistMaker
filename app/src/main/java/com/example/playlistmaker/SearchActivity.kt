package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Display.Mode
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {




    private lateinit var clearBtn: ImageView
    private lateinit var queryInput: EditText
    private lateinit var backBtn: ImageView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyView: LinearLayout
    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var updateBtn: Button
    private lateinit var clearHistoryBtn: Button



    private var text: String = EMPTY_TEXT


    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesServise = retrofit.create(ItunesApi::class.java)
    private val tracks = ArrayList<Track>()

    lateinit var searchAdapter: TrackAdapter
    lateinit var historyAdapter: TrackAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        queryInput = findViewById(R.id.query_input)
        backBtn = findViewById(R.id.search_back_btn)
        searchRecyclerView = findViewById(R.id.search_recyclerView)
        historyRecyclerView = findViewById(R.id.search_history_recyclerView)
        errorPlaceholder = findViewById(R.id.search_placeholder_error)
        nothingFoundPlaceholder = findViewById(R.id.search_placeholder_nothing_found)
        historyView = findViewById(R.id.search_history)
        updateBtn = findViewById(R.id.search_update_button)
        clearBtn = findViewById(R.id.search_clear_button)
        clearHistoryBtn = findViewById(R.id.clear_history_button)

        queryInput.setText(text)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val history = SearchHistory(sharedPreferences)
        history.getHistoryFromSP()

        val onTrackClickListener = object : TrackClickListener{
            override fun onTrackClick(track: Track) {
                history.putTrackToHistory(track)
                historyAdapter.notifyDataSetChanged()
            }
        }
        searchAdapter = TrackAdapter(tracks, onTrackClickListener)
        historyAdapter = TrackAdapter(history.history, onTrackClickListener)

        searchRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchRecyclerView.adapter = searchAdapter

        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.adapter = historyAdapter

        clearHistoryBtn.setOnClickListener {
            history.clear()
            historyAdapter.notifyDataSetChanged()
            historyView.isVisible = false
        }

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                itunesSearch(searchAdapter)
                true
            }
            false
        }


        queryInput.setOnFocusChangeListener { v, hasFocus ->
            historyView.isVisible = queryInput.text.isEmpty() && hasFocus && history.history.isNotEmpty()
            searchRecyclerView.isVisible = !historyView.isVisible
            if(!hasFocus) hideKeyboard()
            if(historyView.isVisible){
                errorPlaceholder.isVisible = false
                nothingFoundPlaceholder.isVisible = false
            }
        }

        updateBtn.setOnClickListener { itunesSearch(searchAdapter) }

        backBtn.setOnClickListener {
            finish()
        }

        clearBtn.setOnClickListener {
            queryInput.setText("")
            hideKeyboard()
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
        }

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.isVisible = !s.isNullOrEmpty()
                text = s.toString()
                historyView.isVisible = s.isNullOrEmpty() && queryInput.hasFocus() && history.history.isNotEmpty()
                searchRecyclerView.isVisible = !historyView.isVisible
                if(historyView.isVisible){
                    errorPlaceholder.isVisible = false
                    nothingFoundPlaceholder.isVisible = false
                }
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








    private fun itunesSearch(searchAdapter: TrackAdapter){
        if(queryInput.text.isNotEmpty()){
            itunesServise.search(queryInput.text.toString()).enqueue(object :
                Callback<ItunesResponse>{
                override fun onResponse(call: Call<ItunesResponse>, response: Response<ItunesResponse>) {
                    Log.d("code", response.code().toString())
                    if (response.isSuccessful) {
                        tracks.clear()
                        searchAdapter.notifyDataSetChanged()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            searchAdapter.notifyDataSetChanged()
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
                        searchAdapter.notifyDataSetChanged()
                        nothingFoundPlaceholder.isVisible = false
                        errorPlaceholder.isVisible = true
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    Log.d("code", t.message.toString())
                    tracks.clear()
                    searchAdapter.notifyDataSetChanged()
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