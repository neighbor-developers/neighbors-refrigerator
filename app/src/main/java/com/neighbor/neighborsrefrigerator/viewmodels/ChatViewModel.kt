package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.Message
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val firebaseDB = FirebaseDatabase.getInstance()
    private val firebaseDBRef = firebaseDB.getReference("chat")

    var messages = MutableStateFlow(
        arrayListOf(
            Message("안녕하세요", "오늘 14:31", true),
            Message("나눔 완료됐나요?", "오늘 14:31", true),
            Message("아직이요", "오늘 14:31", false),
            Message("나눔 받으시겠어요?", "오늘 14:31", false),
            Message("네!", "오늘 14:31", true),
            Message("어디로 가면 될까요?", "오늘 14:31", true),
            Message("여기로 오시면 돼요", "오늘 14:31", false)
        )
    )

    fun newChatRoom(chatId: String){
        firebaseDBRef.child("chat").child(chatId)
    }

    fun reviewPost(id: Int, review: String, rate: Int){
        dbAccessModule.reviewPost(id, review, rate)
    }

}