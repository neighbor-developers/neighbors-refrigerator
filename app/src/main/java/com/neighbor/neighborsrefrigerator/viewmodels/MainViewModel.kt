package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val firebaseDB = FirebaseDatabase.getInstance()

    private val _event = MutableSharedFlow<MainEvent>()
    val event = _event.asSharedFlow()

     suspend fun changeNickname(nickname : String): Boolean {
        val result = dbAccessModule.checkNickname(nickname)
         return if (!result) {
             val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
             UserSharedPreference(App.context()).setUserPrefs("nickname", nickname)
             dbAccessModule.updateNickname(id, nickname)
             updateNicknameInChat(nickname)
             true
         }else{
             false
         }
    }

    private fun updateNicknameInChat(nickname: String) {
        val userId = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
        var usersChatList: List<String> = emptyList()

        viewModelScope.launch {
            viewModelScope.async {
                firebaseDB.reference.child("user").child(userId.toString()).get()
                    .addOnSuccessListener {
                        it.value?.let { it ->
                            usersChatList = it as List<String>
                        }
                        Log.d("유저 채팅 정보 가져오기 성공", usersChatList.toString())

                    }
                    .addOnFailureListener {
                        Log.d("유저 채팅정보 가져오기 실패", usersChatList.toString())
                    }
            }.await()
            usersChatList.forEach {
                firebaseDB.reference.child("chat").child(it).get()
                    .addOnSuccessListener { result ->
                        result.value?.let { data ->
                            val result = data as HashMap<String, Any>?
                            val writer = result?.get("writer") as HashMap<String, Any>?
                            val contact = result?.get("contact") as HashMap<String, Any>?

                            val chatData = FirebaseChatData(
                                result?.get("id") as String,
                                (result["postId"] as Long).toInt(),
                                ChatUserData(
                                    (writer?.get("id") as Long).toInt(),
                                    writer["nickname"] as String,
                                    (writer["level"] as Long).toInt()
                                ),
                                ChatUserData(
                                    (contact?.get("id") as Long).toInt(),
                                    contact["nickname"] as String,
                                    (contact["level"] as Long).toInt()
                                ),
                                result["messages"] as List<ChatMessageData>
                            )
                            if (chatData.writer.id == userId){
                                chatData.writer.nickname = nickname
                            }else{
                                chatData.contact.nickname = nickname
                            }
                            chatData.id?.let {
                                firebaseDB.reference.child("chat").child(chatData.id).setValue(chatData)
                                    .addOnSuccessListener {
                                        Log.d("채팅룸 닉네임 변경 성공", it.toString())
                                    }
                                    .addOnFailureListener {
                                        Log.d("채팅룸 닉네임 변경 실패", it.toString())
                                    }
                            }
                        }
                    }
            }
        }
    }

    fun delChatData(){
        val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
        firebaseDB.reference.child("user").child(id.toString()).removeValue()
    }

    fun sendEmail(postId: Int, content: String) {
        val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
        val data = MailData(id, postId, content)
        dbAccessModule.sendMail(data)
    }

    fun logOut() = viewModelScope.launch {
        _event.emit(MainEvent.LogOut)
    }
    fun delAuth() = viewModelScope.launch {
        _event.emit(MainEvent.DelAuth)
    }

    sealed class MainEvent{
        object LogOut : MainEvent()
        object DelAuth : MainEvent()
    }

}