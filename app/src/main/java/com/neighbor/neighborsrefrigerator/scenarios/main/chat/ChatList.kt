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
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ChatData
import com.neighbor.neighborsrefrigerator.data.RdbChatData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel

@Composable
fun ChatListScreen(navController: NavHostController, chatViewModel: ChatViewModel = viewModel()){
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "채팅목록", textAlign = TextAlign.Center, modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(end = 50.dp), fontSize = 17.sp) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }, modifier = Modifier.size(50.dp) ) {
//                    Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")}
//                },
//
//                backgroundColor = Color.Transparent,
//                elevation = 0.dp
//            )
//        }
//    ) { padding ->
//        Surface(modifier = Modifier.padding(padding)) {
//            val chatList = chatViewModel.chatList.collectAsState()
//            LazyColumn {
//                chatList.value.let { chatList ->
//                    items(chatList){ chat ->
//                        ChatCard(chat = chat, navController, chatViewModel)
//                    }
//                }
//            }
//        }
//    }
//    LaunchedEffect(Unit){
//        chatViewModel.initChatList()
//    }
//}
//
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ChatCard(chat: RdbChatData, navController: NavController, viewModel: ChatViewModel){
//    navController.currentBackStackEntry?.savedStateHandle?.set(key = "chatViewModel", value = viewModel)
//    Card(onClick = {navController.navigate(route = NAV_ROUTE.CHAT.routeName)}) {
//        Row() {
//            Image(
//                painter = painterResource(id = R.drawable.sprout2),
//                contentDescription = "이미지",
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape))
//            Spacer(modifier = Modifier.width(8.dp))
//            Column() {
//                Text(
//                    text = "상대방 닉네임",
//                    color = MaterialTheme.colors.secondaryVariant,
//                    style = MaterialTheme.typography.subtitle2)
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "마지막 채팅 메세지",
//                    style = MaterialTheme.typography.body2)
//            }
//        }
//    }
}

@Composable
@Preview
fun ChatPreview(){

}