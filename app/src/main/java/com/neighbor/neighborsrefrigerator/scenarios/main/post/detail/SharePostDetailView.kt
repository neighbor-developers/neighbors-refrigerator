package com.neighbor.neighborsrefrigerator.scenarios.main.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.scenarios.main.post.ItemImage
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel


@Composable
fun SharePostDetailScreen(navHostController: NavHostController, postViewModel: PostViewModel = viewModel(), post: PostData) {

    val contactUserId by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt())
    }
    val postTime = post.validateDate
    val token = postTime!!.split("T")[0].split("-")

    val day = "${token[0]}년 ${token[1]}월 ${token[2]}일"
    val validateType = when (post.validateType) {
        1 -> "유통기한"
        2 -> "제조일자"
        3 -> "구매일자"
        else -> {
            ""
        }
    }

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
                        Icon(painterResource(id = R.drawable.icon_decl_color), contentDescription = "신고하기", modifier = Modifier.size(25.dp), tint = colorResource(
                            id = R.color.declRed
                        ))
                        Icon(painterResource(id = R.drawable.icon_decl), contentDescription = "신고하기", modifier = Modifier.size(25.dp), tint = Color.White)
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
                DeclarationDialog(postId = post.id!!, type = 1, onChangeState =  {
                    declarationDialogState = false
                })
            }
            var completeShareDialog by remember {
                mutableStateOf(false)
            }

            if(completeShareDialog){
                post.let {
                    CompleteDialog(
                        type = "거래",
                        { completeShareDialog = false },
                        { postViewModel.completeShare(it) }
                    )
                }
            }
            Column {
                Text(text = "상품 자세히 보기", fontSize = 20.sp, modifier = Modifier.padding(start = 30.dp, top = 10.dp, bottom = 20.dp))
                Row() {
                    Spacer(modifier = Modifier.width(30.dp))
                    val modifier = Modifier.size(150.dp)
                    post.productimg1?.let {
                        ItemImage(productImg1 = post.productimg1, modifier = modifier)
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = post.title, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(30.dp))
                Surface(shape = MaterialTheme.shapes.large.copy(topEnd = CornerSize(70.dp))) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xff00ac77))) {
                        Text(text = "내 위치에서 ${post.distance}km", fontSize = 20.sp, color = Color.White)
                        Text(text = "$validateType : $day", fontSize = 20.sp, color = Color.White)
                        if(post.userId != contactUserId) {
                            Button(onClick = {
                                val chatId = post.id.toString() + contactUserId.toString()
                                //postViewModel.newChatRoom(chatId, post.id!!, post.userId)
                                navHostController.navigate("${NAV_ROUTE.CHAT.routeName}/${chatId}/${post.id!!}")
                            }) {
                                Text(text = "채팅하기")
                            }
                        }else{
                            // 포스트 작성자일때 완료 버튼
                            Button(
                                onClick = { completeShareDialog = true },
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.size(85.dp, 33.dp)
                            ) {
                                Text(text = "나눔 완료", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.width(10.dp))

                        }
                    }
                }
            }
        }
    }
}

