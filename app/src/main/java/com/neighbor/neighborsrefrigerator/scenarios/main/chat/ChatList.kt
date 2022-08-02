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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ChatData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel

data class ChatCard(val nickname : String, val msg: String, val type: String)

@Composable
fun ChatListScreen(navController: NavHostController){

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
    ) {
        Surface(modifier = Modifier.padding(it)) {
            //ChatList(chatList = viewModel.chatList.collectAsState(), navController, viewModel)
        }

    }
}

@Composable
fun ChatList(chatList: ArrayList<ChatData>?, navController: NavController) {
    LazyColumn {
//        items(chatList.value){ chat ->
//            // 채팅 데이터 불러오기 (마지막 채팅, 닉네임)
//            ChatCard(chat = chat, navController, viewModel)
//    }
}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatCard(chat: ChatCard, navController: NavController, viewModel: ChatViewModel){
    navController.currentBackStackEntry?.savedStateHandle?.set(key = "chatViewModel", value = viewModel)
    Card(onClick = {navController.navigate(route = NAV_ROUTE.CHAT.routeName)}) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.sprout2),
                contentDescription = "이미지",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Text(
                    text = chat.nickname,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chat.msg,
                    style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
@Preview
fun ChatPreview(){

}