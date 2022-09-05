package com.neighbor.neighborsrefrigerator.scenarios.main.post.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel

@Composable
fun SeekPostDetailScreen(navHostController: NavHostController, postViewModel: PostViewModel = viewModel(), post: PostData) {
    val scaffoldState = rememberScaffoldState()
    var declarationDialogState by remember {
        mutableStateOf(false)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { declarationDialogState = true }) {
                        Icon(painterResource(id = R.drawable.icon_decl), contentDescription = "신고하기", modifier = Modifier.size(45.dp), tint = Color.Red)
                    }
                },
                navigationIcon = {
                    IconButton(onClick =  { navHostController.navigateUp() }) {
                        Icon(
                            painterResource(id = R.drawable.icon_back), contentDescription = "뒤로가기", modifier = Modifier.size(35.dp), tint = colorResource(
                                id = R.color.green)
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            if (declarationDialogState) {
                DeclarationDialog(type = 1) {
                    declarationDialogState = false
                }
            }
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "구함 상세 페이지", fontSize = 30.sp)
                Text(text = "제목 : ${post.title}")

                Button(onClick = {

                    val contactUserId = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
                    // chatId는 포스트 아이디와 접근한 유저 아이디를 합쳐서 만듬
                    val chatId = post.id.toString() + contactUserId.toString()

                    navHostController.navigate("${NAV_ROUTE.CHAT.routeName}/${chatId}/${post.id!!}")
                }, modifier = Modifier.size(60.dp)) {
                    Text(text = "채팅하기")
                }
            }
        }
    }
}
