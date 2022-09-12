package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ChatMessageData
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.FirebaseChatData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalculateTime
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel


@Composable
fun ChatScreen(navController : NavHostController, chatViewModel: ChatViewModel = viewModel(), chatId:String, postId: Int){
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
                        Icon(painterResource(id = R.drawable.icon_back), contentDescription = "뒤로가기", modifier = Modifier.size(35.dp), tint = colorResource(
                            id = R.color.green)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { declarationDialogState = true }) {
                        Icon(painterResource(id = R.drawable.icon_decl), contentDescription = "신고하기", modifier = Modifier.size(45.dp), tint = Color.Red)
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if(declarationDialogState){
                DeclarationDialog(type = 2) {
                    declarationDialogState = false}
            }

            TopBarSection(chatViewModel, navController, chatViewModel.postData.collectAsState(), userId)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                ChatSection(chatViewModel.chatMessages.collectAsState(),chatViewModel.chatData.collectAsState(), userId, Modifier.weight(1f))
                SendSection(chatViewModel, chatId, userId)
            }
        }

    }

    LaunchedEffect(Unit) {
        // 채팅창 들어가서 정보 가져오기- 채팅 데이터, 채팅 내용
        chatViewModel.enterChatRoom(chatId)
        // 채팅 생성한 post 정보 가져오기
        chatViewModel.getPostData(postId)
    }
}
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun TopBarSection(chatViewModel: ChatViewModel, navController: NavHostController, postData: State<PostData?>, userId: Int, alignment: Alignment.Vertical = Alignment.Top) {

    var completeShareDialog by remember {
        mutableStateOf(false)
    }
    val chatData = chatViewModel.chatData.collectAsState()

    if(completeShareDialog){
        postData.value?.let {
            CompleteDialog(
                type = "거래",
                { completeShareDialog = false },
                { chatViewModel.completeShare(it) }
            )
        }
    }
    postData.value?.let { post ->
        var expanded by remember { mutableStateOf(false) }
        Surface(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp),
            onClick = { expanded = !expanded }
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, 150)) with
                            fadeOut(animationSpec = tween(150)) using
                            SizeTransform { initialSize, targetSize ->
                                if (targetState) {
                                    keyframes {
                                        // Expand horizontally first.
                                        IntSize(targetSize.width, initialSize.height) at 150
                                        durationMillis = 300
                                    }
                                } else {
                                    keyframes {
                                        // Shrink vertically first.
                                        IntSize(initialSize.width, targetSize.height) at 150
                                        durationMillis = 300
                                    }
                                }
                            }
                }
            ) { targetExpanded ->
                if (targetExpanded) {
                        ConstraintLayout(
                            Modifier.fillMaxWidth()) {
                            val (title, button) = createRefs()
                            Row(modifier = Modifier
                                .constrainAs(title) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (post.type == 1) { // 나눔일때
                                    Surface(shape = MaterialTheme.shapes.small.copy(CornerSize(10.dp))){
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(post.productimg1)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(70.dp),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(15.dp))
                                }
                                Column{
                                    Text(text = post.title, color = Color.Black, fontSize = 17.sp)
                                    chatData.value?.writer?.nickname?.let {
                                        Text(text = it, color = Color.DarkGray, fontSize = 12.sp)
                                    }
                                }
                            }

                            Row(Modifier.constrainAs(button){
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }) {
                                if (post.userId == userId) {
                                    // 포스트 작성자일때 완료 버튼
                                    Button(onClick = { completeShareDialog = true }) {
                                        Text(text = "나눔 완료")
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                                if (post.type == 1) {
                                    Button(
                                        onClick = {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "post",
                                                value = post
                                            )
                                            navController.navigate(NAV_ROUTE.SHARE_DETAIL.routeName)
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.LightGray)
                                    ) { Text(text = "상세보기") }
                                }
                            }
                        }
                } else {
                    Surface(modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.ShoppingCart, "")
                    }
                }
            }
        }

    }
}

@Composable
fun ChatSection(message: State<List<ChatMessageData>?>, chatData: State<FirebaseChatData?>, userId: Int, modifier: Modifier = Modifier) {
    //userId = 내 아이디
    val writerId: Int? = chatData.value?.writer?.id

    // 상대방 닉네임
    val nickname = if(writerId == userId){
        chatData.value?.contact?.nickname
    }else{
        chatData.value?.writer?.nickname
    }


    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        message.value?.let {
            items(it) { message ->
                nickname?.let { nickname ->
                    MessageItem(message, userId, nickname)
                    Spacer(modifier = Modifier.height(13.dp))
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessageData, userId: Int, nickname: String) {
    val current = System.currentTimeMillis()

    val calculateTime = CalculateTime()
    val time = calculateTime.calTimeToChat(current, message.createdAt)


    // 본인일때 true
    val isMe = message.from == userId
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.from != userId) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isMe) {
            Text(text = time, color = Color.Gray, modifier = Modifier.padding(start = 7.dp, end = 7.dp), fontSize = 13.sp)
        }
        Column {
            if (!isMe) {
                Row{
                    Text(text = nickname, color = Color.Black, fontSize = 13.sp, modifier = Modifier.padding(bottom = 5.dp, end = 5.dp))
                    Image(
                        painter = painterResource(R.drawable.level3_ver2),
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
                    Text(text = message.content, color = Color.Black)
                }
            }
        }
        if (!isMe) {
            Text(text = time, color = Color.Gray, modifier = Modifier.padding(start = 7.dp), fontSize = 13.sp)
        }
    }
}

@Composable
fun SendSection(viewModel: ChatViewModel, chatId: String, userId: Int) {
    val sendMessage = remember {
        mutableStateOf("")
    }
    val timestamp = remember {
        mutableStateOf<Long>(0)
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
                            timestamp.value = System.currentTimeMillis()
                            val message = ChatMessageData(sendMessage.value, false, timestamp.value, userId)
                            viewModel.newMessage(chatId, message)
                            sendMessage.value = ""
                        }
                    }
                ){
                    Icon(imageVector = Icons.Filled.Send,
                        contentDescription = "보내기", tint = colorResource(id = R.color.green))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = colorResource(id = R.color.green))
        )
    }
}
