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

    fun setUserPrefs(person: UserData) {
        prefs.edit().putString("id", person.id.toString())
        prefs.edit().putString("fbId", person.fbID)
        prefs.edit().putString("email", person.email)
        prefs.edit().putString("nickname", person.nickname)
        prefs.edit().putString("addressMain", person.addressMain)
        prefs.edit().putString("addressDetail", person.addressDetail)
        prefs.edit().putString("reportPoint", person.reportPoint.toString())
        prefs.edit().putString("latitude", person.latitude.toString())
        prefs.edit().putString("longitude", person.longitude.toString())
        prefs.edit().putString("createdAt", person.createdAt)
    }
}