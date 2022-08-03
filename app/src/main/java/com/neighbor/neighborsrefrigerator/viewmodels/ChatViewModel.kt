package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel : ViewModel() {

    val dbAccessModule = DBAccessModule()

    fun reviewPost(id: Int, review: String, rate: Int){
        dbAccessModule.reviewPost(id, review, rate)
    }

}