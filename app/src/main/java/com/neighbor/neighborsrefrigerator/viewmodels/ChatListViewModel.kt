package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.ContentValues
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.neighbor.neighborsrefrigerator.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalculateTime
import com.neighbor.neighborsrefrigerator.utilities.MyTypeConverters
import kotlin.io.path.createTempDirectory

// 채팅 하나마다 안읽은 메세지 수, 마지막 채팅, 상대방 닉네임 및 정보 가져와야함

// 최근 채팅 순으로 정렬
// chatListDetail에 리스트 정보 저장 방법
// 가장 최근 채팅 먼저 올리기

class ChatListViewModel: ViewModel() {
    private var example: List<FirebaseChatData> = listOf(
        FirebaseChatData(
            id = "",
            postId = 1,
            writer = ChatUserData(id = 1, nickname = "서연", 1),
            contact = ChatUserData(id = 2, nickname = "zinkiki", 2),
            messages = listOf(ChatMessageData(content = "안녕하세요", false, 1662288446, 2),
                ChatMessageData(content = "안녕하세요", false, 1662288446, 2),
                ChatMessageData(content = "안녕하세요", true, 1662288446, 2),
                ChatMessageData(content = "안녕하세요", true, 1662288446, 2))
        ),
    )

    val chatListData = MutableStateFlow<List<FirebaseChatData>>(emptyList())
    var usersChatList = mutableListOf<String>("")
    private val firebaseDB = FirebaseDatabase.getInstance()


    // 실시간으로 유저의 채팅 리스트 변화 감지 -> 추가되면 추가된 상태로 정렬 다시
    fun initChatList(){
        val userId = UserSharedPreference(App.context()).getUserPrefs("id").toString()

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let{
                    val _usersChatList = it as List<String>
                    usersChatList = _usersChatList as MutableList<String>

                    usersChatList.forEach { chatId ->
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
                    val _chatData = FirebaseChatData(
                        result?.get("id") as String,
                        (result["postId"] as Long).toInt(),
                        ChatUserData((writer?.get("id") as Long).toInt(), writer["nickname"] as String, (writer["level"] as Long).toInt()),
                        ChatUserData((contact?.get("id") as Long).toInt(), contact["nickname"] as String, (contact["level"] as Long).toInt()),
                        result["messages"] as List<ChatMessageData>
                    )

                    if(usersChatList.isNotEmpty()){
                        // 있는 데이터인지 찾아보고 있으면 삭제 후 맨앞, 없으면 그냥 맨앞에 추가
                        chatListData.value?.let {
                            chatListData.value.plus(_chatData)
                        }
                    } else{
                        chatListData.value = listOf(_chatData)
                    }
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
        return lastChat?.createdAt ?: null
    }
}
