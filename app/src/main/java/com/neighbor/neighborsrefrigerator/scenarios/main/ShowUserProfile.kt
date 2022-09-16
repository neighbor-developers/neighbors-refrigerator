package com.neighbor.neighborsrefrigerator.scenarios.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.ReviewData
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalLevel
import com.neighbor.neighborsrefrigerator.view.ChangeNicknameDialog
import com.neighbor.neighborsrefrigerator.view.FlowerDialog
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ShowUserProfile(navController: NavHostController, userId: Int, viewModel: MainViewModel = viewModel()) {
    val dbAccessModule by remember {
        mutableStateOf(DBAccessModule())
    }
    var postData : ArrayList<PostData> by remember {
        mutableStateOf(arrayListOf())
    }
    var userData : UserData? by  remember {
        mutableStateOf(null)
    }
    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.Main).launch {
            if (userId != 0) {
                dbAccessModule.getPostByUserId(userId){postData = it}
                userData = dbAccessModule.getUserInfoById(userId)[0]
            }
        }
    }
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "프로필", modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 30.dp), fontSize = 15.sp, textAlign = TextAlign.Center, color = Color.Black)
                        },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painterResource(id = R.drawable.icon_back),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(35.dp),
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            userData?.let { UserDataScreen(it, navController, postData, viewModel) }
        }
    }
}

@Composable
fun UserDataScreen(userData: UserData, navController: NavHostController, postData: ArrayList<PostData>, viewModel: MainViewModel){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val calLevel = CalLevel()
        val level = calLevel.GetUserLevel(postData)
        val flowerVer = UserSharedPreference(App.context()).getLevelPref("flowerVer")

        val reviewData: ArrayList<ReviewData> by remember {
            mutableStateOf(arrayListOf())
        }
        var showNicknameDialog by remember { mutableStateOf(false) }
        if (showNicknameDialog) {
            ChangeNicknameDialog(
                changeDialogState = { showNicknameDialog = it },
                viewModel
            )
        }
        var flowerDialogState by remember { mutableStateOf(false) }
        if (flowerDialogState)
            FlowerDialog { flowerDialogState = false }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Text(
                text = "마이페이지", fontSize = 30.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 100.dp, bottom = 70.dp)
            )
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (nicknameText, button) = createRefs()
                Row(
                    modifier = Modifier.constrainAs(nicknameText) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "'${userData.nickname}'", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            " 님의 레벨 : ",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Image(
                            painter = painterResource(
                                when (level) {
                                    2 ->
                                        when (flowerVer) {
                                            1 -> R.drawable.level2_ver1
                                            2 -> R.drawable.level2_ver2
                                            3 -> R.drawable.level2_ver3
                                            else -> R.drawable.level1
                                        }
                                    3 ->
                                        when (flowerVer) {
                                            1 -> R.drawable.level3_ver1
                                            2 -> R.drawable.level3_ver2
                                            3 -> R.drawable.level3_ver3
                                            else -> R.drawable.level1
                                        }
                                    4 ->
                                        when (flowerVer) {
                                            1 -> R.drawable.level4_ver1
                                            2 -> R.drawable.level4_ver2
                                            3 -> R.drawable.level4_ver3
                                            else -> R.drawable.level1
                                        }
                                    else -> R.drawable.level1
                                }
                            ),
                            contentDescription = "level icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.constrainAs(button) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    }, horizontalAlignment = Alignment.End
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            showNicknameDialog = true
                        },
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Icon(
                                Icons.Filled.Settings,
                                "contentDescription",
                                tint = colorResource(id = R.color.green),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 5.dp)
                            )
                            Text(text = "닉네임 바꾸기", fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.clickable {
                            flowerDialogState = true
                        },
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Icon(
                                Icons.Filled.Settings,
                                "contentDescription",
                                tint = colorResource(id = R.color.green),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 5.dp)
                            )
                            Text(text = "꽃 변경하기", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.fillMaxWidth()
        ) {
            Surface(modifier = Modifier.weight(1f)) {
                ShowPostList(postData, navController)
            }
            Surface(modifier = Modifier.weight(1f)) {
                ShowReviewList(postData, userData, reviewData)
            }
        }
    }
}

@Composable
fun ShowPostList(post: ArrayList<PostData>, navController: NavHostController) {
    Column(
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Title(title = "게시글 목록")
        if (post.isEmpty()){
            Text(text = "게시한 글이 없습니다",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(10.dp)
            )
        }
         else {
            LazyColumn {
                items(post) { post ->
                    Text(
                        post.title,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = "post",
                                    value = post
                                )
                                navController.navigate(route = NAV_ROUTE.SHARE_DETAIL.routeName)
                            }
                            .padding(10.dp)
                    )
                    Divider()
                }
            }
        }
    }
}


@Composable
fun ShowReviewList(post: ArrayList<PostData>, userData: UserData, review: ArrayList<ReviewData>) {

    Column(
    ) {
        Title(title = "후기 목록")
        if (post.isEmpty()){
            Text(text = "등록된 후기가 없습니다",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(10.dp)
            )

        } else {
            LazyColumn {
                items(review) { review ->
                    Text(
                        review.review,
                        color = Color.DarkGray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Divider()
                }
            }
        }
    }
}