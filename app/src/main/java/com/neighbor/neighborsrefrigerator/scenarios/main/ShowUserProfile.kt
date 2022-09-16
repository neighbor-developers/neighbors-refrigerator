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
        val nickname = UserSharedPreference(App.context()).getUserPrefs("nickname")
        val postCount = postData.count { true }
        val calLevel = CalLevel()
        val level = calLevel.GetUserLevel(postData)
        val userId: Int = userData.id!!
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
                modifier = Modifier.padding(top = 40.dp, bottom = 50.dp)
            )
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (nicknameText, button) = createRefs()
                if (nickname != null) {
                    Text(nickname, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, modifier = Modifier.constrainAs(nicknameText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                }
                Button(
                    modifier = Modifier.constrainAs(button) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                    onClick = {
                        showNicknameDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "닉네임 바꾸기")
                        Icon(
                            Icons.Filled.Settings,
                            "contentDescription",
                            tint = colorResource(id = R.color.green),
                            modifier = Modifier
                                .size(22.dp)
                                .padding(start = 5.dp)
                        )
                    }
                }
            }
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (nicknameText, button) = createRefs()
                if (nickname != null) {
                    Text(nickname, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, modifier = Modifier.constrainAs(nicknameText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                }

                Button(
                    onClick = {
                        flowerDialogState = true
                    },
                    modifier = Modifier.size(60.dp).constrainAs(button) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                                                                        },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        disabledBackgroundColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
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
                        contentDescription = "level icon"
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "레벨", fontSize = 14.sp)


        }
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                text = "거래 횟수: $postCount",
                style = MaterialTheme.typography.overline,
                color = Color.DarkGray,
                fontSize = 25.sp,
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ShowPostList(postData, userId)
                ShowReviewList(postData, userId, reviewData)
            }
        }
    }
}

/*@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int
) {
    var ratingState by remember { mutableStateOf(rating) }

    Row(
        modifier = modifier
            .padding(30.dp, 0.dp, 0.dp, 0.dp)
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(33.dp)
                    .height(33.dp)
                    .clickable {
                        ratingState = i
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
        Text(
            text = "3.7점",
            style = MaterialTheme.typography.overline,
            color = Color.LightGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .padding(5.dp, 9.dp, 0.dp, 0.dp)
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }
}*/

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ShowPostList(post: ArrayList<PostData>, userId: Int?) {
    var dbAccessModule by remember {
        mutableStateOf(DBAccessModule())
    }

    var userData : UserData? by  remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.Main).launch {
            if (userId != 0) {
                userData = dbAccessModule.getUserInfoById(userId!!)[0]
            }
        }
    }

    Column(
        modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, Color.LightGray))
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = userData?.nickname + " 님의 거래 내역",
            color = Color.DarkGray,
            fontSize = 25.sp
        )

        if (post.isEmpty()) {
            Text(
                text = "아직 올린 게시물이 없어요!",
                color = Color.DarkGray,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
        } else {
            LazyColumn {
                items(post) { post ->
                    Text(
                        post.title,
                        color = Color.DarkGray,
                        fontSize = 18.sp
                    )
                    Divider()
                }
            }
        }
    }
}


@Composable
fun ShowReviewList(post: ArrayList<PostData>, userId: Int?, review: ArrayList<ReviewData>) {
    var dbAccessModule by remember {
        mutableStateOf(DBAccessModule())
    }

    var userData : UserData? by  remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.Main).launch {
            if (userId != 0) {
                userData = dbAccessModule.getUserInfoById(userId!!)[0]
            }
        }
    }



    Column(
        modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, Color.LightGray)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = userData?.nickname + " 님의 후기 내역",
            color = Color.DarkGray,
            fontSize = 25.sp
        )
        if (post.isEmpty()) {
            Text(
                text = "아직 후기 내역이 없어요!",
                color = Color.DarkGray,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
        } else {
            LazyColumn {
                items(review) { review ->
                    Text(
                        review.review,
                        color = Color.DarkGray,
                        fontSize = 18.sp
                    )
                    Divider()
                }
            }
        }
    }
}