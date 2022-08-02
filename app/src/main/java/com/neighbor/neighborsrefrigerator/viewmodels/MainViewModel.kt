package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainViewModel: ViewModel() {

    private val dbAccessModule = DBAccessModule()

    val hasFbId = MutableStateFlow(true)

    fun checkUserAccount(fbId : String){
        dbAccessModule.hasFbId(fbId){ hasFbId.value = it}
    }
}