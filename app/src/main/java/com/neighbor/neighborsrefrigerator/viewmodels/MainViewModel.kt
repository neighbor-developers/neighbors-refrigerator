package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val auth = FirebaseAuth.getInstance()
    val hasId = MutableStateFlow(true)

    init {
        dbAccessModule.hasFbId(auth.currentUser.toString()) { hasId.value = it }
    }


}