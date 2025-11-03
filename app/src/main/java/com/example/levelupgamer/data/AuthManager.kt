package com.example.levelupgamer.data

import android.content.Context
import android.content.SharedPreferences
import com.example.levelupgamer.data.model.User
import com.google.gson.Gson

class AuthManager(context: Context, private val gson: Gson) {

    private val prefs: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    companion object {
        private const val USER_KEY = "LOGGED_IN_USER"
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        editor.putString(USER_KEY, userJson)
        editor.apply()
    }

    fun loadUser(): User? {
        val userJson = prefs.getString(USER_KEY, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun clearUser() {
        editor.remove(USER_KEY)
        editor.apply()
    }
}