package com.neighbor.neighborsrefrigerator.viewmodels

import android.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.neighbor.neighborsrefrigerator.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import java.sql.Timestamp
import java.time.LocalDateTime


class ChatListViewModel: ViewModel() {
    var chatData = MutableStateFlow<List<Chat>?>(null) // 임시로 String으로 설정
    var chatList = MutableStateFlow<ArrayList<Chat>?>(null)
    var nickname = MutableStateFlow<String>("")
    var lastMessage = MutableStateFlow<String>("")
    var newMessage = MutableStateFlow<Int>(0)
    var createAt = MutableStateFlow<Timestamp>(Timestamp(2022,8,23,16,19,20,10))


    private lateinit var db: ChatListDB


    fun initChatList(){
        CoroutineScope(Dispatchers.Main).launch {
            val chats = CoroutineScope(Dispatchers.IO).async{
                db.chatListDao()!!.getChatMessage(UserSharedPreference(App.context()).getUserPrefs("id")!!)
            }.await()
            chatData.value = chats
        }
/*        chatData.value?.forEach {

            refreshChatList(it)

        }*/
    }

    fun refreshChatList(chat: Chat){
        // 채팅 하나마다 안읽은 메세지 수, 마지막 채팅, 상대방 닉네임 및 정보 가져와야함
        // 상대방 정보 -> contactId 체크해서 본인 아니면 postId로 postData 가져와서 작성자 정보 가져와야함

        // 최근 채팅 순으로 정렬
        // chatListDetail에 리스트 정보 저장 방법
        // 가장 최근 채팅 먼저 올리기
        checkNewMessage(chat)
        checkLastChat(chat)
        getUserData(chat)
    }

    private fun checkNewMessage(chat: Chat){
        newMessage.value = 0
        chat.message.forEach {
            if(!it.isRead){
                newMessage.value++
            }
        }
    }
    private fun checkLastChat(chat: Chat){
        chat.message
        // 마지막 메세지 기준 알기
    }
    private fun getUserData(chat: Chat){
        // 상대방 정보 -> contactId 체크해서 본인 아니면 postId로 postData 가져와서 작성자 정보 가져와야함
        if(chat.contact.id == UserSharedPreference(App.context()).getUserPrefs("id")){

        }
        // 아닐 경우 writerId 체크해서 상대방 정보 가져오기
    }
}