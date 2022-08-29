package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.ChatListViewModel


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
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
            val chatRemove = chatListViewModel.chatData
            LazyColumn {
                chatList.value.let{ chatlist ->
                    itemsIndexed(items = chatlist!!, key={_, listItem ->
                        listItem.hashCode()
                    }){ index, chat ->
                        // https://www.youtube.com/watch?v=Q89i4iZK8ko
                        val state = rememberDismissState(
                            confirmStateChange = {
                                if(it == DismissValue.DismissedToStart){
                                    // https://stackoverflow.com/questions/73201881/modifying-a-mutablestateflow-list-in-kotlin
                                    chatRemove.value?.toMutableList()?.remove(chat)
                                }
                                true
                            }
                        )
                        SwipeToDismiss(state = state, background = {
                            val color = when(state.dismissDirection){
                                DismissDirection.StartToEnd -> Color.Transparent
                                    DismissDirection.EndToStart -> Color.Black
                                    null -> Color.Magenta
                            }
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(color = color)
                                .padding(10.dp)){
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete",
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    tint = Color.Gray
                                )
                            }
                        },
                        dismissContent = {
                            ChatCard(chat = chat.chatData!!, navController, chatListViewModel)
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
