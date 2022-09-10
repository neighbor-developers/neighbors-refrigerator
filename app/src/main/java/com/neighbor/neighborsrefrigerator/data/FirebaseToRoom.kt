package com.neighbor.neighborsrefrigerator.data

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FirebaseToRoom {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private var roomDb = ChatListDB.getInstance(App.context())
    var usersChatList = mutableListOf<String>("")
    val chatListData = MutableStateFlow<List<ChatData>>(emptyList())


    fun getChatID(){
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
/*                    val _chatData = FirebaseChatData(
                        result?.get("id") as String,
                        (result["postId"] as Long).toInt(),
                        ChatUserData((writer?.get("id") as Long).toInt(), writer["nickname"] as String, (writer["level"] as Long).toInt()),
                        ChatUserData((contact?.get("id") as Long).toInt(), contact["nickname"] as String, (contact["level"] as Long).toInt()),
                        result["messages"] as List<ChatMessageData>
                    )*/
                    val _chatData = ChatData(
                        ChatDetail(
                            result?.get("postId") as String,
                            ChatUserData((writer?.get("id") as Long).toInt(), writer["nickname"] as String, (writer["level"] as Long).toInt()),
                            ChatUserData((contact?.get("id") as Long).toInt(), contact["nickname"] as String, (contact["level"] as Long).toInt()),
                            result["messages"] as List<ChatMessageData>
                        ),
                        result?.get("id") as String
                    )

                    if(usersChatList.isNotEmpty()){
                        // 있는 데이터인지 찾아보고 있으면 삭제 후 맨앞, 없으면 그냥 맨앞에 추가
                        chatListData.value?.let {
                            chatListData.value.plus(_chatData)
/*                            chatListData.value.sortedWith(
                                compareBy { getLastChatTimestamp(it) }
                            )*/
                        }
                    } else{
                        chatListData.value = listOf(_chatData)
                    }
                    Log.d("firebase", chatListData.value.toString())
                    /*chatListData.value?.forEach{
                        CoroutineScope(Dispatchers.IO).launch{
                            roomDb?.chatListDao()?.insert(chatList = it)
                        }
                        Log.d("firebase to room -> ", roomDb?.chatListDao()?.getChatMessage().toString())
                    }

                    Log.d("리스트", chatListData.value.toString())*/
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child("chat").child(chatId).addValueEventListener(chatListener)
    }

    private fun getLastChatTimestamp(chatData: ChatData): Long?{

        // 마지막 메세지 기준 - 더 최근일수록 숫자가 커짐
        val lastChat = chatData.chatData?.message?.maxWithOrNull(compareBy { it.createdAt})

        // 임시로 널 값 설정, 후에 바꾸기
        // 아무런 메세지가 없을 때 어떻게 할지 정하기
        return lastChat?.createdAt ?: null
    }

    private fun getNewChat(){

    }
}