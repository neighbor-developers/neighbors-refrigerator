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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewModel() : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val firebaseDB = FirebaseDatabase.getInstance()
    var chatMessages = MutableStateFlow<List<ChatMessageData>>(emptyList())
    var chatData = MutableStateFlow<FirebaseChatData?>(null)
    var postData = MutableStateFlow<PostData?>(null)
    private var usersChatList = MutableStateFlow<List<String>>(emptyList())

    init {
        val userId = UserSharedPreference(App.context()).getUserPrefs("id").toString()
        firebaseDB.reference.child("user").child(userId).get()
            .addOnSuccessListener {
                it.value?.let { it ->
                    usersChatList.value = it as List<String>
                }
                Log.d("유저 채팅 정보 가져옴", usersChatList.value.toString())
            }
            .addOnFailureListener {
            }
    }

    // 채팅방 들어감
    fun enterChatRoom(chatId: String){

        // 한번도 채팅하지 않은경우는 조회 불가
        Log.d("채팅방 id", chatId)
        firebaseDB.reference.child("chat").child(chatId).get()
            .addOnSuccessListener {
                Log.d("채팅방 정보", it.value.toString())
                it.value?.let { value ->
                    val result = value as HashMap<String, Any>?
                    val writer = result?.get("writer") as HashMap<String, Any>?
                    val contact = result?.get("contact") as HashMap<String, Any>?
                    val _chatData = FirebaseChatData(
                        result?.get("id") as String,
                        (result["postId"] as Long).toInt(),
                        ChatUserData((writer?.get("id") as Long).toInt(), writer["nickname"] as String, (writer["level"] as Long).toInt()),
                        ChatUserData((contact?.get("id") as Long).toInt(), contact["nickname"] as String, (contact["level"] as Long).toInt()),
                        result["messages"] as List<ChatMessageData>
                    )
                    chatData.value = _chatData
                }
            }
            .addOnFailureListener{
                Log.d("채팅룸 정보 가져오기 실패", it.toString())
            }

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val _chatMessage = arrayListOf<ChatMessageData>()
                val messageData = snapshot.value as ArrayList<HashMap<String, Any>>?

                Log.d("변화 리스너", snapshot.value.toString())
                messageData?.forEach {
                    _chatMessage.add(
                        ChatMessageData(
                            it["content"] as String,
                            it["_read"] as Boolean,
                            it["createdAt"] as Long,
                            (it["from"] as Long).toInt()
                        )
                    )
                }
                chatMessages.value = arrayListOf()
                chatMessages.value = _chatMessage.toList()
                chatData.value?.let {
                    readMessage(chatId, chatMessages.value)
                }
                Log.d("변화 리스너2", chatMessages.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child("chat").child(chatId).child("messages").addValueEventListener(chatListener)
    }

    fun readMessage(chatId: String, chatDataToRead: List<ChatMessageData>){
        val last = chatDataToRead.lastIndex
        chatDataToRead[last].is_read = true
        firebaseDB.reference.child("chat").child(chatId).child("messages").setValue(chatDataToRead)
            .addOnSuccessListener {
                Log.d("newChatRoomSuccess", "메세지 보내기 성공")
            }
            .addOnFailureListener{
                Log.d("메세지 보내기 실패", it.toString())
            }

    }

    fun newMessage(chatId: String, messageData: ChatMessageData){
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))
        val userPrefs = UserSharedPreference(App.context())
        if(chatMessages.value.isEmpty()) {
            // 첫 메세지일때 채팅방 생성
            dbAccessModule.makeChat(chatId,postData.value!!.id!!,userPrefs.getUserPrefs("id")!!.toInt(),timeStamp)
            dbAccessModule.sendMessage(chatId,timeStamp,messageData.content,messageData.from,userPrefs.getNoticePref("chat"))
            chatMessages.value = listOf(messageData)
            newChatRoom(chatId, postId = postData.value!!.id!!, postData.value!!.userId, chatMessages.value)
            Log.d("빈 리스트에 추가됨", chatMessages.value.toString())
        }else{
            chatMessages.value += messageData
            Log.d("추가됨", chatMessages.value.toString())
            dbAccessModule.sendMessage(chatId,timeStamp,messageData.content,messageData.from,userPrefs.getNoticePref("chat"))
            firebaseDB.reference.child("chat").child(chatId).child("messages").setValue(chatMessages.value)
                .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "메세지 보내기 성공")
                }
                .addOnFailureListener{
                    Log.d("메세지 보내기 실패", it.toString())
                }
        }
    }

    private fun newChatRoom(chatId: String, postId: Int, writerId: Int, message :List<ChatMessageData>){
        val userId = UserSharedPreference(App.context()).getUserPrefs("id").toString()

        viewModelScope.launch {
            if (chatId !in usersChatList.value) {
                val writerData: java.util.ArrayList<UserData> = dbAccessModule.getUserInfoById(writerId)
                val writer = ChatUserData(writerData[0].id, writerData[0].nickname, writerData[0].reportPoint)

                val contactData = UserSharedPreference(App.context()).getUserPrefs()
                val contact = ChatUserData(contactData.id, contactData.nickname, contactData.reportPoint)

                val rdbChatData = FirebaseChatData(chatId, postId, writer, contact, message)

                firebaseDB.reference.child("chat").child(chatId).setValue(rdbChatData)
                    .addOnSuccessListener {
                        Log.d("newChatRoomSuccess", "채팅룸 생성 완료")
                        enterChatRoom(chatId)
                    }
                    .addOnFailureListener {
                        Log.d("채팅룸 생성 실패", it.toString())
                    }

                Log.d("유저 채팅 리스트1", usersChatList.value.toString())
                if (usersChatList.value.isEmpty()){
                    usersChatList.value = listOf(chatId)
                    Log.d("리스트 빔", usersChatList.value.toString())
                }else {
                    usersChatList.value += chatId
                    Log.d("리스트 안빔", usersChatList.value.toString())
                }
                Log.d("유저 채팅 리스트", usersChatList.value.toString())

                firebaseDB.reference.child("user").child(userId).setValue(usersChatList.value)
                    .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "유저 정보에 추가 완료")
                    enterChatRoom(chatId)
                }
                    .addOnFailureListener {
                        Log.d("유저 정보 추가 실패", it.toString())
                    }

            }
        }
    }

    // 리뷰 작성
    fun reviewPost(id: Int, review: String, rate: Int){
        dbAccessModule.reviewPost(id, review, rate)
    }

    // 포스트 데이타 가져오기
    fun getPostData(postId: Int){
        dbAccessModule.getPostByPostId(postId){postData.value = it[0] }
    }



}