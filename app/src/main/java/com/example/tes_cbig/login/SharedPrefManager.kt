package com.example.tes_cbig.login

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "MyAppPrefs"
        private const val KEY_TOKEN = "token"

        @Volatile
        private var INSTANCE: SharedPrefManager? = null

        fun getInstance(context: Context): SharedPrefManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefManager(context).also { INSTANCE = it }
            }
        }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(KEY_TOKEN) // Menghapus token dari SharedPreferences
        editor.apply()
    }
}