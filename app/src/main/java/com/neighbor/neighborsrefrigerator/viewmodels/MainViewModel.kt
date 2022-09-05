package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val auth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseDatabase.getInstance()

    private val _event = MutableSharedFlow<MainEvent>()
    val event = _event.asSharedFlow()

    var emailContent = MutableStateFlow("")
    var userEmail = MutableStateFlow("")

//    suspend fun nickname(): String{
//        val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
//        return if(dbAccessModule.getUserInfoById(auth.currentUser.toString()) != arrayListOf<UserData>()){
//            dbAccessModule.getUserInfoById(id)[0].nickname
//        }else{
//            Log.d("닉네임",  "알수없음")
//            "알수없음"
//        }
//    }

     suspend fun changeNickname(nickname : String): Boolean {
        val result = dbAccessModule.checkNickname(nickname)
         return if (!result) {
             val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
             UserSharedPreference(App.context()).setUserPrefs("nickname", nickname)
             dbAccessModule.updateNickname(id, nickname)
             true
         }else{
             false
         }
    }

    fun delChatData(){
        val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
        firebaseDB.reference.child("user").child(id.toString()).removeValue()
    }

    fun sendEmail(content: String, email: String) = viewModelScope.launch {
        emailContent.value = content
        userEmail.value = email
        _event.emit(MainEvent.SendEmail) }

    fun logOut() = viewModelScope.launch {
        _event.emit(MainEvent.LogOut)
    }
    fun delAuth() = viewModelScope.launch {
        _event.emit(MainEvent.DelAuth)
    }

    sealed class MainEvent{
        object SendEmail : MainEvent()
        object LogOut : MainEvent()
        object DelAuth : MainEvent()
    }

}