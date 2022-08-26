package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel() : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val firebaseDB = FirebaseDatabase.getInstance()

    var chatMessages = MutableStateFlow<List<RdbMessageData>>(emptyList())
    var chatData = MutableStateFlow<RdbChatData?>(null)
    var postData = MutableStateFlow<PostData?>(null)

    fun enterChatRoom(chatId: String){
        // 기본적으로 룸디비, 와이파이 될때 rdb와 비교??
        firebaseDB.reference.child(chatId).get()
            .addOnSuccessListener {
                Log.d("채팅방 정보", it.value.toString())
                val result = it.value as HashMap<String, Any>?
                val _chatData = RdbChatData(
                        result?.get("id") as String,
                        (result["postId"] as Long).toInt(),
                        result["writer"] as Map<String, RdbUserData>,
                        result["contact"] as Map<String, RdbUserData>,
                        result["message"] as ArrayList<RdbMessageData>
                    )
                chatData.value = _chatData
            }
            .addOnFailureListener{
                Log.d("채팅룸 정보 가져오기 실패", it.toString())
            }

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val _chatMessage = arrayListOf<RdbMessageData>()
                val messageData = snapshot.value as ArrayList<HashMap<String, Any>>?

                messageData?.forEach {
                    _chatMessage.add(
                        RdbMessageData(
                            it["content"] as String,
                            it["_read"] as Boolean,
                            it["createdAt"] as String,
                            (it["from"] as Long).toInt()
                        )
                    )
                }
                chatMessages.value = arrayListOf()
                chatMessages.value = _chatMessage.toList()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child(chatId).child("message").addValueEventListener(chatListener)
    }

     fun newChatRoom(chatId: String, postId: Int, writerId: Int){
        // roomDb에 저장된 채팅이 있는지 확인->진행되고 있는 채팅이 있음에도 채팅하기 버튼 누를 가능성 있음
        // RDB ChatId 찾아보고 없으면 RDB 저장
        if (true) { // 없을때
            viewModelScope.launch {
                val writerData : ArrayList<UserData> = dbAccessModule.getUserInfoById(writerId)
                val writerRdbData = RdbUserData(writerData[0].id, writerData[0].nickname, writerData[0].reportPoint)
                val writer = mapOf("writer" to writerRdbData)

                val contactData =UserSharedPreference(App.context()).getUserPrefs()
                val contactRdbData = RdbUserData(contactData.id, contactData.nickname, contactData.reportPoint)
                val contact = mapOf("contact" to contactRdbData)

                val rdbChatData = RdbChatData(chatId, postId, writer, contact, arrayListOf())

                firebaseDB.reference.child(chatId).setValue(rdbChatData)
                    .addOnSuccessListener {
                        Log.d("newChatRoomSuccess", "채팅룸 생성 완료")
                    }
                    .addOnFailureListener{
                        Log.d("채팅룸 생성 실패", it.toString())
                    }
            }
        } else {
            /*no-op*/
        }
    }

    fun newMessage(chatId: String, messageData: RdbMessageData){
        if(chatMessages.value.isEmpty()) {
            chatMessages.value = arrayListOf(messageData)
            Log.d("빈 리스트에 추가됨", chatMessages.value.toString())
        }else{
            chatMessages.value += messageData
            Log.d("추가됨", chatMessages.value.toString())
        }

        firebaseDB.reference.child(chatId).child("messages").setValue(chatMessages.value)
            .addOnSuccessListener {
                Log.d("newChatRoomSuccess", "메세지 보내기 성공")
            }
            .addOnFailureListener{
                Log.d("메세지 보내기 실패", it.toString())
            }
    }

    // 리뷰 작성
    fun reviewPost(id: Int, review: String, rate: Int){
        dbAccessModule.reviewPost(id, review, rate)
    }

    // 포스트 데이타 가져오기
    fun getPostData(){
        chatData.value?.let { data ->
            dbAccessModule.getPostByPostId(data.postId){postData.value = it[0] }
        }
    }

    // 판매완료 요청
    fun completeShare(postData: PostData){
        dbAccessModule.completeTrade(postData)
    }

}