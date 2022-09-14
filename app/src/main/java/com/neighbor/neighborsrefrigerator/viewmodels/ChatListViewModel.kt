package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.neighbor.neighborsrefrigerator.data.ChatMessageData
import com.neighbor.neighborsrefrigerator.data.ChatUserData
import com.neighbor.neighborsrefrigerator.data.FirebaseChatData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.flow.MutableStateFlow

// 채팅 하나마다 안읽은 메세지 수, 마지막 채팅, 상대방 닉네임 및 정보 가져와야함

// 최근 채팅 순으로 정렬
// chatListDetail에 리스트 정보 저장 방법
// 가장 최근 채팅 먼저 올리기

class ChatListViewModel: ViewModel() {

    val chatListData = MutableStateFlow<List<FirebaseChatData>>(emptyList())
    var usersChatList = MutableStateFlow<List<String>>(emptyList())
    private val firebaseDB = FirebaseDatabase.getInstance()


    // 실시간으로 유저의 채팅 리스트 변화 감지 -> 추가되면 추가된 상태로 정렬 다시
    init {
        val userId = UserSharedPreference(App.context()).getUserPrefs("id").toString()

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let{
                    val _usersChatList = it as List<String>
                    usersChatList.value = _usersChatList
                    Log.d("userChatList1", _usersChatList.toString())

                    usersChatList.value.forEach { chatId ->
                        getChatList(chatId)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child("user").child(userId).addValueEventListener(chatListener)
    }

    private fun getChatList(chatId: String){
        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let { value ->
                    val result = value as HashMap<String, Any>?
                    val writer = result?.get("writer") as HashMap<String, Any>?
                    val contact = result?.get("contact") as HashMap<String, Any>?
                    Log.d("파이어베이스 message", writer.toString())
                    val _messageData = result?.get("messages") as ArrayList<HashMap<String, Any>>?
                    Log.d("파이어베이스 message", _messageData.toString())

                    var messageData : ArrayList<ChatMessageData>? = null

                    if (!_messageData.isNullOrEmpty()) {
                            _messageData.forEach { data ->
                                if (messageData.isNullOrEmpty()){
                                    messageData = arrayListOf(
                                        ChatMessageData(
                                            createdAt = data["createdAt"] as Long,
                                            is_read = data["_read"] as Boolean,
                                            content = data["content"] as String,
                                            from = (data["from"] as Long).toInt()))
                                }else {
                                    messageData!!.add(
                                        ChatMessageData(
                                            createdAt = data["createdAt"] as Long,
                                            is_read = data["_read"] as Boolean,
                                            content = data["content"] as String,
                                            from = (data["from"] as Long).toInt()))
                            }
                        }
                    }
                    val _chatData = FirebaseChatData(
                        result?.get("id") as String,
                        (result["postId"] as Long).toInt(),
                        ChatUserData((writer?.get("id") as Long).toInt(), writer["nickname"] as String, (writer["level"] as Long).toInt()),
                        ChatUserData((contact?.get("id") as Long).toInt(), contact["nickname"] as String, (contact["level"] as Long).toInt()),
                        messageData!!.toList()
                    )


                    if (chatListData.value.isNullOrEmpty()){
                        chatListData.value = listOf(_chatData)
                        Log.d("chatList 생성", chatListData.value.toString())
                    }else{
                        chatListData.value += _chatData
                        Log.d("chatList 추가", chatListData.value.toString())
                    }

//                    if(usersChatList.value.isNotEmpty()){
//                        // 있는 데이터인지 찾아보고 있으면 삭제 후 맨앞, 없으면 그냥 맨앞에 추가
//
//                        chatListData.value.let {
//                            chatListData.value = chatListData.value + chatListData.value.plus(_chatData)
//                            chatListData.value.sortedWith(compareBy { getLastChatTimestamp(it) })
//                        }
//                    } else{
//                        chatListData.value = listOf(_chatData)
//                    }
                    Log.d("파이어베이스", chatListData.value.toString())

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("파이어베이스 에러", "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child("chat").child(chatId).addValueEventListener(chatListener)
    }

    private fun getLastChatTimestamp(chatData: FirebaseChatData): Long?{

        // 마지막 메세지 기준 - 더 최근일수록 숫자가 커짐
        val lastChat = chatData.messages?.maxWithOrNull(compareBy { it.createdAt})

        // 임시로 널 값 설정, 후에 바꾸기
        // 아무런 메세지가 없을 때 어떻게 할지 정하기
        return lastChat?.createdAt
    }
}
