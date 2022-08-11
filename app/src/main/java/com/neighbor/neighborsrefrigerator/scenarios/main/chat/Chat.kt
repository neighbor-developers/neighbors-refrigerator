package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel
import java.text.SimpleDateFormat

data class Message(
    val text: String,
    val createdAt: String,
    val who: Boolean
)

@Composable
fun ChatScreen(navController : NavHostController){

    val viewModel = ChatViewModel()

    var declarationDialogState by remember {
        mutableStateOf(false)
    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { declarationDialogState = true }) {
                        Icon(Icons.Filled.Warning, contentDescription = "신고하기", tint = Color.Red)
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if(declarationDialogState){
                DeclarationDialog(type = 2) {
                    declarationDialogState = false
                }
            }
            TopBarSection("고구마 나눠요")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                ChatSection(viewModel.messages.collectAsState(), Modifier.weight(1f))
                SendSection(viewModel)
            }
        }

    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarSection(postTitle: String, alignment: Alignment.Vertical = Alignment.Top) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        onClick = { /* 해당 포스트로 이동 */ },
        elevation = 5.dp,
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp)) {
            if (true) { // 나눔일때
                Icon(imageVector = Icons.Filled.Face, contentDescription = "상품 이미지", modifier = Modifier.size(45.dp))
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(/* 상품 이미지 */)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(100.dp)
//                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = postTitle, color = Color.Black, fontSize = 17.sp)
                Text(text = "찌니", color = Color.DarkGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ChatSection(message: State<ArrayList<Message>>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        items(message.value) { item ->
            MessageItem(message = item)
            Spacer(modifier = Modifier.height(13.dp))
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (!message.who) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (message.who) {
            Text(text = message.createdAt, color = Color.Gray, modifier = Modifier.padding(end = 7.dp), fontSize = 13.sp)
        }
        if (message.text != "") {
            Box(
                modifier = if (!message.who) {
                    Modifier
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
                        )
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                } else {
                    Modifier
                        .background(
                            color = Color.Yellow,
                            shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                        )
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                }
            ) {
                Text(text = message.text, color = Color.Black)
            }
        }
        if (!message.who) {
            Text(text = message.createdAt, color = Color.Gray, modifier = Modifier.padding(start = 7.dp), fontSize = 13.sp)
        }
    }
}

@Composable
fun SendSection(viewModel: ChatViewModel) {
    var sendMessage by remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = sendMessage,
            onValueChange = { sendMessage = it },
            placeholder = { Text(text = "메세지를 작성해주세요") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (sendMessage != "") {
                            sendMessage = ""
                        }
                    }
                ){
                    Icon(imageVector = Icons.Filled.Send,
                        contentDescription = "보내기",)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatScreen(navController = rememberNavController())
}