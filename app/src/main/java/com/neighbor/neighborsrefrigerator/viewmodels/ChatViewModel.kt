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
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel() : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val firebaseDB = FirebaseDatabase.getInstance()

    var chatMessages = MutableStateFlow<List<RdbMessageData>?>(null)
    var chatData = MutableStateFlow<RdbChatData?>(null)
    var postData = MutableStateFlow<PostData?>(null)

    var chatList = MutableStateFlow<List<RdbChatData>?>(null) // 임시로 String으로 설정

    // chatList
    fun initChatList(){
        // roomDb의 채팅 목록 가져오기
        chatList.value?.forEach{
            // 채팅 하나마다 안읽은 메세지 수, 마지막 채팅, 상대방 닉네임 및 정보 가져와야함
            // 상대방 정보 -> contactId 체크해서 본인 아니면 postId로 postData 가져와서 작성자 정보 가져와야함
            checkNewMessage(it)
            getUserData(it.postId)
        }
    }

    private fun checkNewMessage(chat: RdbChatData){
        // 와이파이 되는지 확인 후 되면 파이어베이스, 안되면 룸디비
        var savedInRdb : List<RdbMessageData>? = null
        val savedRoomDb : List<RdbMessageData>
        // 될지 모르겠음 ㅠㅠ
        firebaseDB.reference.child(chat.id!!).child("message").get()
            .addOnSuccessListener {
                savedInRdb = it.value as List<RdbMessageData>
            }
            .addOnFailureListener{
                Log.d("메세지 정보 가져오기 실패", it.toString())
            }
        // roomDb 에서 채팅 ㄱ져오기
        // 룸db랑 비교 -> 안읽은 채팅 메세지 수 체크
        // roomDB에는 채팅창 들어가서 조회하는 순간에 저장해야함

    }
    private fun getUserData(postId: Int){
        dbAccessModule.getPostByPostId(postId)
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
        // roomDb에 저장된 채팅이 있는지 확인->진행되고 있는 채팅이 있음에도 채팅하기 버튼 누를 가능성 있음
        // RDB ChatId 찾아보고 없으면 RDB 저장
        if (true) { // 없을때
            val rdbChatData = RdbChatData(chatId, postId, contactId)
            // 어떤형식으로 저장되는지 헷갈려서 두개로 나눠서 만들어놓음
            // 후에 바꿀 수 있음
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
        // 아직 RDB 사용법을 제대로 파악 못해서 어떤 형식으로 저장할지 미정, 배열로 할지
        chatMessages.value?.plus(messageData)
        firebaseDB.reference.child(chatId).child("message").setValue(chatMessages.value)
            .addOnSuccessListener {
                Log.d("newChatRoomSuccess", "채팅룸 메세지 목록 생성 완료")
            }
            .addOnFailureListener{
                Log.d("채팅룸 메세지 테이블 생성 실패", it.toString())
            }
    }

    // 리뷰 작성
    fun reviewPost(id: Int, review: String, rate: Int){
        dbAccessModule.reviewPost(id, review, rate)
    }

    fun getPostData(){
        chatData.value?.let {
            //dbAccessModule.getPostByPostId(it.postId){postData.value = it}
        }
    }

    fun completeShare(postId: Int?){
        postId?.let {
            val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))
            //dbAccessModule
        }
    }

}