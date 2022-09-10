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

    val chatListData = MutableStateFlow<List<FirebaseChatData>>(example)



    // 실시간으로 유저의 채팅 리스트 변화 감지 -> 추가되면 추가된 상태로 정렬 다시
    init {


    }
}
