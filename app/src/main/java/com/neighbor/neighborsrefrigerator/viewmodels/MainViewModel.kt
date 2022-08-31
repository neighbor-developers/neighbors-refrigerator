package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val auth = FirebaseAuth.getInstance()
    val hasId = MutableStateFlow(true)

    private val _event = MutableSharedFlow<MainEvent>()
    val event = _event.asSharedFlow()

    var emailContent = MutableStateFlow("")
    var userEmail = MutableStateFlow("")

    init {
        dbAccessModule.hasFbId(auth.currentUser.toString()) { hasId.value = it }
    }

     suspend fun changeNickname(nickname : String): Boolean{
        val result = dbAccessModule.checkNickname(nickname)
         return if (result) {
             val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
             UserSharedPreference(App.context()).setUserPrefs("nickname", nickname)
             dbAccessModule.updateNickname(id, nickname)
             true
         }else{
             false
         }
    }

    fun sendEmail(content: String, email: String) = viewModelScope.launch {
        emailContent.value = content
        userEmail.value = email
        _event.emit(MainEvent.SendEmail) }

    fun toStartActivity() = viewModelScope.launch {_event.emit(MainEvent.ToStartActivity)}

    sealed class MainEvent{
        object SendEmail : MainEvent()
        object ToStartActivity : MainEvent()
    }

}