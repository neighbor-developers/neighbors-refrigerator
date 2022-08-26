package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.neighbor.neighborsrefrigerator.data.ChatListData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.ChatListViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel

@Composable
fun ChatListScreen(navController: NavHostController, chatListViewModel: ChatListViewModel = viewModel()){
    val chatListViewModel = chatListViewModel
    chatListViewModel.initChatList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "채팅목록", textAlign = TextAlign.Center, modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp), fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }, modifier = Modifier.size(50.dp) ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")}
                },

                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            // composable에서 컴포지션이 일어날 때 suspend fun을 실행해주는 composable
            LaunchedEffect(Unit){
                chatListViewModel.initChatList()
            }
            val chatList = chatListViewModel.chatData.collectAsState()
            LazyColumn {
                chatList.value.let { it ->
                    items(it!!){ chat ->
                        ChatCard(chat = chat.chatData!!, navController, chatListViewModel)
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatCard(chat: Chat, navController: NavController, viewModel: ChatListViewModel){
    viewModel.refreshChatList(chat)

    var nickname = viewModel.nickname.collectAsState()
    var lastMessage = viewModel.lastMessage.collectAsState()
    var createAt = viewModel.createAt.collectAsState()
    var newMessage = viewModel.newMessage.collectAsState()

    navController.currentBackStackEntry?.savedStateHandle?.set(key = "chatViewModel", value = viewModel)
    Card(onClick = {navController.navigate(route = NAV_ROUTE.CHAT.routeName)}) {
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

@Composable
@Preview
fun ChatPreview(){
    ChatListScreen(rememberNavController())
}