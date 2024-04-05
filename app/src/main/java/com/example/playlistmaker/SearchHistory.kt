package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson


private const val HISTORY = "history"
private const val UNITS_COUNT = 10 // количество треков в истории
class SearchHistory(private val sharedPreferences: SharedPreferences) {


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
        if(history.size > UNITS_COUNT) history.removeAt(UNITS_COUNT)

        putHistoryToSP()
    }

    fun clear(){
        history.clear()
        putHistoryToSP()
    }


}