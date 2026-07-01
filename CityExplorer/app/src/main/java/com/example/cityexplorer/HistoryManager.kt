package com.example.cityexplorer

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HistoryManager {
    private const val PREFS_NAME = "city_explorer_history"
    private const val HISTORY_KEY = "completed_challenges"

    fun saveChallenge(context: Context, challenge: Challenge) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()

        val currentHistoryJson = prefs.getString(HISTORY_KEY, "[]")
        val type = object : TypeToken<MutableList<Challenge>>() {}.type
        val history: MutableList<Challenge> = gson.fromJson(currentHistoryJson, type)

        history.add(0, challenge)

        prefs.edit().putString(HISTORY_KEY, gson.toJson(history)).apply()
    }

    fun getHistory(context: Context): List<Challenge> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val currentHistoryJson = prefs.getString(HISTORY_KEY, "[]")
        val type = object : TypeToken<List<Challenge>>() {}.type
        return gson.fromJson(currentHistoryJson, type)
    }
}