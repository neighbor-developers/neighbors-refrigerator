package com.neighbor.neighborsrefrigerator.scenarios.main.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.scenarios.main.post.ItemImage
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel


@Composable
fun SharePostDetailScreen(navHostController: NavHostController, postViewModel: PostViewModel = viewModel(), post: PostData) {

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
                        Icon(Icons.Filled.Warning, contentDescription = "", tint = Color.Red)
                    }
                },
                navigationIcon = {
                    IconButton(onClick =  { navHostController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
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
            Column {
                Text(text = "상품 자세히 보기", fontSize = 20.sp, modifier = Modifier.padding(start = 30.dp, top = 10.dp, bottom = 20.dp))
                Row() {
                    Spacer(modifier = Modifier.width(30.dp))
                    val modifier = Modifier.size(150.dp)
                    post.productimg1?.let {
                        ItemImage(productimg1 = post.productimg1, modifier = modifier)
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
                    }
                }
                val contactUserId by remember {
                    mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt())
                }
                if(post.userId != contactUserId) {
                    Button(onClick = {
                        val chatId = post.id.toString() + contactUserId.toString()
                        //postViewModel.newChatRoom(chatId, post.id!!, post.userId)
                        navHostController.navigate("${NAV_ROUTE.CHAT.routeName}/${chatId}/${post.id!!}")
                    }) {
                        Text(text = "채팅하기")
                    }
                }
            }
        }
    }
}
//
//@Preview
//@Composable
//fun ShareDetailPreview(){
//    SharePostDetailScreen(navHostController = rememberNavController(), post = PostData(
//        id = 3,
//        title = "고구마 나눠요",
//        categoryId = "100",
//        userId = 3,
//        content = "생각보다 많이 남을 것 같아서 나눠요",
//        type = 1,
//        mainAddr = "산기대학로 233",
//        addrDetail = "E동",
//        validateType = 1,
//        validateDate = "2022/3/24",
//        createdAt = "2022-07-29 13:23:22",
//        latitude = 33.4,
//        longitude = 127.4,
//        state = "판매",
//        rate = null,
//        review = null,
//        validateImg = null,
//        productimg1 = null,
//        productimg2 = null,
//        productimg3 = null,
//        updatedAt = null,
//        completedAt = null,
//        distance = null
//    )
//    )
//}
