package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.Chat
import com.neighbor.neighborsrefrigerator.data.RdbChatData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.ChatListViewModel


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatListScreen(navController: NavHostController, chatListViewModel: ChatListViewModel =ChatListViewModel()){
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
            val chatRemove = chatListViewModel.chatListData


            LazyColumn {
                chatList.value.let{ chatlist ->
                    itemsIndexed(items = chatlist!!, key={_, listItem ->
                        listItem.hashCode()
                    }){ index, chat ->
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
                                        chatRemove.value?.toMutableList()?.remove(chat)
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
                                    DismissValue.DismissedToStart -> painterResource(R.drawable.ic_check_red)
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
                                        .background(Color(240,240,240))
                                        .padding(horizontal = 30.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        modifier = Modifier.scale(scale),
                                        imageVector = Icons.Filled.Delete,
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

                /*chatList.value.let { it ->
                    items(it!!){ chat ->
                        ChatCard(chat = chat.chatData!!, navController, chatListViewModel)
                    }
                }*/
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatCard(chat: RdbChatData, navController: NavController, viewModel: ChatListViewModel){
    viewModel.refreshChatList(chat)

    var nickname = viewModel.nickname.collectAsState()
    var lastMessage = viewModel.lastMessage.collectAsState()
    var createAt = viewModel.createAt.collectAsState()
    var newMessage = viewModel.newMessage.collectAsState()

    Card(onClick = {navController.navigate(route = "${NAV_ROUTE.CHAT.routeName}/${chat.id}/${chat.postId}")}) {
        Column() {
            Row() {
                Column() {
                    Image(
                        painter = painterResource(id = R.drawable.sprout2),
                        contentDescription = "async 이미지",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = nickname.value,
                        color = MaterialTheme.colors.secondaryVariant,
                        style = MaterialTheme.typography.subtitle2)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lastMessage.value,
                        style = MaterialTheme.typography.body2)
                }
                Text(
                    text= createAt.value.toString(),
                    style = MaterialTheme.typography.body2
                )
            }
            Text(
                text = newMessage.value.toString(),
                style = MaterialTheme.typography.body2
            )
        }
    }
}
