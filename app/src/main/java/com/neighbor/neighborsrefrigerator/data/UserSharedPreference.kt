package com.neighbor.neighborsrefrigerator.data

import android.content.Context
import android.content.SharedPreferences

class UserSharedPreference(context: Context) {
    private val prefsFilename = "UserPrefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    fun getUserPrefs(key: String): String? {
        return prefs.getString(key, "")
    }

    fun getUserPrefs(): UserData {
        return UserData(
            id = prefs.getString("id", "")!!.toInt(),
            fbID = prefs.getString("fbID", "")!!,
            email = prefs.getString("email", "")!!,
            nickname = prefs.getString("nickname", "")!!,
            addressMain = prefs.getString("addressMain", "")!!,
            addressDetail = prefs.getString("addressDetail", "")!!,
            reportPoint = prefs.getString("reportPoint", "")!!.toInt(),
            latitude = prefs.getString("latitude", "")!!.toDouble(),
            longitude = prefs.getString("longitude", "")!!.toDouble(),
            createdAt = prefs.getString("createdAt", "")!!
        )
    }

    fun setUserPrefs(key:String, value: String) {
        return prefs.edit().putString(key, value).apply()
    }

    fun setNicknamePrefs(nickname : String) {
        prefs.edit().putString("nickname", nickname).apply()
    }

    fun setUserPrefs(person: UserData) {
        prefs.edit().putString("id", person.id.toString()).apply()
        prefs.edit().putString("fbId", person.fbID).apply()
        prefs.edit().putString("email", person.email).apply()
        prefs.edit().putString("nickname", person.nickname).apply()
        prefs.edit().putString("addressMain", person.addressMain).apply()
        prefs.edit().putString("addressDetail", person.addressDetail).apply()
        prefs.edit().putString("reportPoint", person.reportPoint.toString()).apply()
        prefs.edit().putString("latitude", person.latitude.toString()).apply()
        prefs.edit().putString("longitude", person.longitude.toString()).apply()
        prefs.edit().putString("createdAt", person.createdAt).apply()
    }
}