package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel() : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val firebaseDB = FirebaseDatabase.getInstance()

    var chatMessages = MutableStateFlow<List<RdbMessageData>?>(null)
    var chatData = MutableStateFlow<RdbChatData?>(null)
    var postData = MutableStateFlow<PostData?>(null)

    var chatList = MutableStateFlow<List<RdbChatData>?>(null) // 임시로 String으로 설정

    fun initChatList(){
        // roomDb의 채팅 목록 가져오기
        chatList.value?.forEach{
            checkNewMessage(it)
        }
    }

    fun checkNewMessage(chat: RdbChatData){
        var savedInRdb : List<RdbMessageData>? = null
        val savedRoomDb : List<RdbMessageData>
        firebaseDB.reference.child(chat.id!!).child("message").get()
            .addOnSuccessListener {
                savedInRdb = it.value as List<RdbMessageData>
            }
            .addOnFailureListener{
                Log.d("메세지 정보 가져오기 실패", it.toString())
            }
        // 룸db랑 비교 -> 안읽은 채팅 메세지 수 체크

    }

    fun enterChatRoom(chatId: String){
        // 와이파이 되는지 확인 후 되면 파이어베이스, 안되면 룸디비
        firebaseDB.reference.child(chatId).get()
            .addOnSuccessListener {
                chatData.value = it.value as RdbChatData
            }
            .addOnFailureListener{
                Log.d("채팅룸 정보 가져오기 실패", it.toString())
            }

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatMessages.value = snapshot.value as List<RdbMessageData>?
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child(chatId).child("message").addValueEventListener(chatListener)
    }

    fun newChatRoom(chatId: String, postId: Int, contactId: Int){

        // roomDb에 저장된 채팅이 있는지 확인

        // RDB ChatId 찾아보고 없으면 RDB 저장
        if (true) {
            val rdbChatData = RdbChatData(chatId, postId, contactId)
            firebaseDB.reference.child(chatId).setValue(rdbChatData)
                .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "채팅룸 생성 완료")
                }
                .addOnFailureListener{
                    Log.d("채팅룸 생성 실패", it.toString())
                }

            firebaseDB.reference.child(chatId).child("message").setValue(null)
                .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "채팅룸 메세지 목록 생성 완료")
                }
                .addOnFailureListener{
                    Log.d("채팅룸 메세지 테이블 생성 실패", it.toString())
                }
        } else {
            /*no-op*/
        }

    }

    fun newMessage(chatId: String, messageData: RdbMessageData){
        chatMessages.value?.plus(messageData)
        firebaseDB.reference.child(chatId).child("message").setValue(chatMessages.value)
            .addOnSuccessListener {
                Log.d("newChatRoomSuccess", "채팅룸 메세지 목록 생성 완료")
            }
            .addOnFailureListener{
                Log.d("채팅룸 메세지 테이블 생성 실패", it.toString())
            }
    }

    fun reviewPost(id: Int, review: String, rate: Int){
        dbAccessModule.reviewPost(id, review, rate)
    }

    fun getPostData(){
        chatData.value?.let {
            //dbAccessModule.getPostByPostId(it.postId){postData.value = it}
        }
    }

}