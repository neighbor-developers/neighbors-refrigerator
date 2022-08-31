package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.utilities.App

class DrawerProfileViewModel() : ViewModel() {

    fun NicknameUpdate(nickname : String) {
        val userPrefs = UserSharedPreference(App.context())

        userPrefs.setNicknamePrefs(nickname)
    }
}