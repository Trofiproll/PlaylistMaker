package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val HISTORY = "history"
    val history = ArrayList<Track>()

    fun getHistoryFromSP(){
        val json = sharedPreferences.getString(HISTORY, null) ?: return
        history.clear()
        history.addAll(Gson().fromJson(json, Array<Track>::class.java))
    }

    private fun putHistoryToSP(){
        sharedPreferences.edit()
            .putString(HISTORY, Gson().toJson(history))
            .apply()
    }

    fun putTrackToHistory(track: Track){
        getHistoryFromSP()

        for(i in history){
            if(i.trackId == track.trackId) {
                history.remove(i)
                break
            }
        }

        history.add(0, track)
        if(history.size > 10) history.removeAt(10)

        putHistoryToSP()
    }

    fun clear(){
        history.clear()
        putHistoryToSP()
    }


}