package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ChatScreen(navController : NavHostController, chatViewModel: ChatViewModel = viewModel(), chatId:String){
    val userId by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt())
    }

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
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            if(declarationDialogState){
                DeclarationDialog(type = 2) {
                    declarationDialogState = false}
            }

            TopBarSection(chatViewModel, navController, chatViewModel.postData.collectAsState(), userId)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                ChatSection(chatViewModel.chatMessages.collectAsState(), userId, Modifier.weight(1f))
                SendSection(chatViewModel, userId, chatId)
            }
        }

    }

    LaunchedEffect(Unit) {
        // 채팅창 들어가서 정보 가져오기- 채팅 데이터, 채팅 내용
        chatViewModel.enterChatRoom(chatId)
        // 채팅 생성한 post 정보 가져오기
        chatViewModel.getPostData()
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarSection(chatViewModel: ChatViewModel, navController: NavHostController, postData: State<PostData?>, userId: Int, alignment: Alignment.Vertical = Alignment.Top) {

    var completeShareDialog by remember {
        mutableStateOf(false)
    }
    /*
     postData 가져와서 보여주고, 거래 완료 null에, 작성자면 판매완료 버튼 생성하고, 실시간으로 변경도 가능하게끔 해야함
    */
    if(completeShareDialog){
        postData.value?.let {
            CompleteDialog(
                type = "거래",
                { completeShareDialog = false },
                { chatViewModel.completeShare(it) }
            )
        }
    }
    postData.value?.let { postData ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp),
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = postData)
                if(postData.productImg != null){
                    navController.navigate(NAV_ROUTE.SHARE_DETAIL.routeName)
                }else{
                    navController.navigate(NAV_ROUTE.SEEK_DETAIL.routeName)
                }
                      },
            elevation = 5.dp,
            backgroundColor = Color.LightGray,
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp)) {
    //            if (true) { // 나눔일때
    //                Icon(imageVector = Icons.Filled.Face, contentDescription = "상품 이미지", modifier = Modifier.size(45.dp))
    //                AsyncImage(
    //                    model = ImageRequest.Builder(LocalContext.current)
    //                        .data(/* 상품 이미지 */)
    //                        .crossfade(true)
    //                        .build(),
    //                    contentDescription = null,
    //                    contentScale = ContentScale.Crop,
    //                    modifier = Modifier.size(100.dp)
    ////                )
    //            }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = postData.title, color = Color.Black, fontSize = 17.sp)
                    Text(text = "${postData.userId}", color = Color.DarkGray, fontSize = 12.sp)
                }
                if (postData.userId == userId) {
                    // 포스트 작성자일때 완료 버튼
                    Button(onClick = { completeShareDialog = true }) {
                        Text(text = "나눔 완료")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatSection(message: State<List<RdbMessageData>?>, userId: Int, modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        message.value?.let {
            items(it) { message ->
                MessageItem(message, userId)
                Spacer(modifier = Modifier.height(13.dp))
            }
        }
    }
}

@Composable
fun MessageItem(message: RdbMessageData, userId: Int) {
    val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).parse(message.createdAt)
    val time = SimpleDateFormat("hh:MM", Locale.KOREA).format(formattedTime!!)

    // 본인일때 true
    val isMe = message.from == userId
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.from != userId) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isMe) {
            Text(text = time, color = Color.Gray, modifier = Modifier.padding(start = 7.dp), fontSize = 13.sp)
        }
        Column {
            if (!isMe) {
                Row{
                    Text(text = "떼잉", color = Color.Black, fontSize = 13.sp, modifier = Modifier.padding(bottom = 5.dp, end = 5.dp))
                    Image(
                        painter = painterResource(R.drawable.sprout),
                        contentDescription = "App icon",
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(17.dp)
                    )
                }
            }
            if (message.content != "") {
                Box(
                    modifier = if (isMe) {
                        Modifier
                            .background(
                                color = Color.Yellow,
                                shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                            )
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                    } else {
                        Modifier
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
                            )
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                    }
                ) {
                    Text(text = message.content!!, color = Color.Black)
                }
            }
        }
        if (!isMe) {
            Text(text = time, color = Color.Gray, modifier = Modifier.padding(start = 7.dp), fontSize = 13.sp)
        }
    }
}

@Composable
fun SendSection(viewModel: ChatViewModel, userId: Int, chatId: String) {
    val sendMessage = remember {
        mutableStateOf("")
    }
    val timestamp = remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = sendMessage.value,
            onValueChange = { sendMessage.value = it },
            placeholder = { Text(text = "메세지를 작성해주세요") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        // 메세지 보내기
                        if (sendMessage.value.isNotEmpty()) {
                            Log.d("새로운 메세지", sendMessage.value)
                            timestamp.value = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))
                            val message = RdbMessageData(sendMessage.value, false, timestamp.value, userId)
                            viewModel.newMessage(chatId, message)
                            sendMessage.value = ""
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
