package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ChatMessageData
import com.neighbor.neighborsrefrigerator.data.FirebaseChatData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.ChatListViewModel
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("UnrememberedMutableState", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatListScreen(navController: NavHostController){
    val chatListViewModel by remember {
        mutableStateOf(ChatListViewModel())
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "채팅목록", textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp), fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }, modifier = Modifier.size(50.dp) ) {
                        Icon(painterResource(id = R.drawable.icon_back), contentDescription = "뒤로가기", modifier = Modifier.size(35.dp), tint = colorResource(
                            id = R.color.green)
                        )}
                },

                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        
        Surface(modifier = Modifier.padding(padding)) {

            val chatList = chatListViewModel.chatListData.collectAsState()
            var userChatList = chatListViewModel.usersChatList.collectAsState()

            LazyColumn {
                itemsIndexed(items = chatList.value){ index, chat ->
                    // https://www.youtube.com/watch?v=Q89i4iZK8ko
                    val dismissState = rememberDismissState(
                        confirmStateChange = { dismissValue ->
                            when (dismissValue) {
                                DismissValue.Default -> { // dismissThresholds 만족 안한 상태
                                    false
                                }
                                DismissValue.DismissedToEnd ->{
                                    false
                                }
                                DismissValue.DismissedToStart -> { // <- 방향 스와이프 (삭제)
                                    Log.d("유저챗리스트1", userChatList.toString())
                                    chatListViewModel.delChat(index)
                                    Log.d("유저챗리스트2", userChatList.toString())
                                    Log.d("인덱스", index.toString())
                                    true
                                }
                            }
                        })

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        background = { // dismiss content
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> backgroundColor.copy(alpha = 0.5f) // dismissThresholds 만족 안한 상태
                                    DismissValue.DismissedToEnd -> Color.Green.copy(alpha = 0.4f) // -> 방향 스와이프 (수정)
                                    DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.5f) // <- 방향 스와이프 (삭제)
                                }
                            )
                            val icon = when (dismissState.targetValue) {
                                DismissValue.Default -> painterResource(R.drawable.ic_check_green)
                                DismissValue.DismissedToEnd -> painterResource(R.drawable.ic_check_green)
                                DismissValue.DismissedToStart -> painterResource(R.drawable.delete_button)
                            }
                            val scale by animateFloatAsState(
                                when (dismissState.targetValue == DismissValue.Default) {
                                    true -> 0.8f
                                    else -> 1.5f
                                }
                            )
                            val alignment = when (direction) {
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 30.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    modifier = Modifier.scale(scale),
                                    painter = icon,
                                    contentDescription = null
                                )
                            }
                        },
                        dismissContent = {
                            ChatCard(chat = chat, navController, chatListViewModel)
                        },
                        directions = setOf(DismissDirection.EndToStart))
                    Divider()
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatCard(chat: FirebaseChatData, navController: NavController, viewModel: ChatListViewModel){
    // chat 내용이 바뀔 때마다 chatcard가 업데이트 되게 하


    val nickname = getNickname(chat)
    val lastMessage = checkLastMessage(chat)
    val newMessage = checkNewMessage(chat)

    Card(
        onClick = {navController.navigate(route = "${NAV_ROUTE.CHAT.routeName}/${chat.id}/${chat.postId}")},
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row() {
            Column() {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.level4_ver3),
                        contentDescription = "async 이미지",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .fillMaxWidth(fraction = 0.3f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(fraction = 0.2f),
                        text = nickname,
                        color = MaterialTheme.colors.secondaryVariant,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    lastMessage?.let {
                        Text(
                            modifier = Modifier.fillMaxWidth(fraction = 0.4f),
                            text = it.content,
                            style = MaterialTheme.typography.body2
                        )
                        val time = SimpleDateFormat("hh:mm", Locale.KOREA).format(it.createdAt)
                        Text(
                            text =time.toString(),    //   아래
                            style = MaterialTheme.typography.body2
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.fillMaxWidth(fraction = 1f),
                text = "${newMessage}개", //  옆
                style = MaterialTheme.typography.body2
            )
        }
    }
}

fun checkLastTime(chatData: FirebaseChatData): String? {
    // 마지막 메세지 기준 - 더 최근일수록 숫자가 커짐

    //val lastChat = chatData.messages?.maxWithOrNull(compareBy { it.createdAt })
    val lastChat = if (chatData.messages.isNotEmpty()){
        chatData.messages[chatData.messages.size-1]
    }else{
        null
    }
    val current = System.currentTimeMillis()
    val lastChatTimeStamp = lastChat?.createdAt

/*        compareBy<ChatMessage>{ it.created_at?.let { it ->
            MyTypeConverters().convertDateToTimeStamp(
                it
            )
        } }*/

    return ""
}

fun checkLastMessage(chatData: FirebaseChatData): ChatMessageData?{
    //val lastChat = chatData.messages?.maxWithOrNull(compareBy { it.createdAt })
    val lastChat = if (chatData.messages.isNotEmpty()){
        chatData.messages[chatData.messages.size-1]
    }else{
        null
    }
    return lastChat
}

fun getNickname(chatData: FirebaseChatData): String{
    // 상대방 정보 -> contactId 체크해서 본인 아니면 postId로 postData 가져와서 작성자 정보 가져와야함
    // int? String? 에러날 수 있음 + 널체크 어떻게?
    var nickname: String = ""
    if(chatData.writer?.nickname == UserSharedPreference(App.context()).getUserPrefs("nickname")){
        nickname = chatData.contact!!.nickname
    }
    else if(chatData.contact!!.nickname == UserSharedPreference(App.context()).getUserPrefs("nickname")){
        nickname = chatData.writer!!.nickname
    }
    return nickname
    // 아닐 경우 writerId 체크해서 상대방 정보 가져오기
}

fun checkNewMessage(chatData: FirebaseChatData):Int{
    var newMessage: Int = 0

    chatData.messages.forEach {
        if(!it.is_read){
            newMessage++
        }
    }
    Log.d("새로운 메세지",newMessage.toString())

    return newMessage
}
