package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
import com.neighbor.neighborsrefrigerator.utilities.CalculateTime
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SeekPostScreen(
    postViewModel: PostViewModel,
    navHostController: NavHostController
) {
    val nickname = UserSharedPreference(App.context()).getUserPrefs("nickname")
    Column() {
        DrawLine()
        Row(modifier = Modifier
            .padding(start = 15.dp, bottom = 10.dp, top = 10.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "",
                tint = colorResource(id = R.color.green),
                modifier = Modifier.size(18.dp))
            Text(
                text = "${nickname}님 주변의 소식",
                modifier = Modifier.padding(start = 5.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        DrawLine()
        SeekPostList(postViewModel.seekPostsByTime.collectAsLazyPagingItems(), navHostController = navHostController, viewModel = postViewModel)
    }
}
@Composable
fun SeekPostList(posts: LazyPagingItems<PostData>,
                 navHostController: NavHostController,
                 viewModel: PostViewModel
){
    val scrollState = LazyListState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.backgroundGray))) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.backgroundGray)),
            state = scrollState
        ){

            items(posts){
                if (it != null) {
                    SeekItem(post = it, navHostController = navHostController, viewModel)
                }
            }
        }
    }
}
@Composable
fun SeekItem(post: PostData,
             navHostController: NavHostController,
             viewModel: PostViewModel) {

    var userData: UserData? by remember {
        mutableStateOf(null)
    }

    var level by remember {
        mutableStateOf(0)
    }
    val userFlower = UserSharedPreference(App.context()).getLevelPref("flowerVer")
    val current = System.currentTimeMillis()
    val calTime = CalculateTime()
    val time = calTime.calTimeToPost(current, post.createdAt)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            userData = viewModel.getUserData(post.userId)
            level = viewModel.getUserLevel(post.userId)
        }
    }
    Column {
        Spacer(modifier = Modifier.height(10.dp).background(Color.Transparent))
        Card(modifier = Modifier.fillMaxWidth().background(Color.White), elevation = 1.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ConstraintLayout(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 17.dp, start = 20.dp, end = 17.dp)
                ) {
                    val (title, timeText) = createRefs()
                    //Text(text = post.title, fontSize = 15.sp,
                    Text(text = post.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.constrainAs(title) {
                            start.linkTo(parent.start)
                        }
                    )
                    Text(text = time, fontSize = 10.sp, color = Color.DarkGray,
                        modifier = Modifier.constrainAs(timeText) {
                            end.linkTo(parent.end)
                        }
                    )
                }

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp)) {
                    //Text(text = post.content, fontSize = 12.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
                    Text(
                        text = post.content,
                        fontSize = 13.5.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(
                            start = 5.dp,
                            end = 3.dp,
                            top = 20.dp,
                            bottom = 30.dp
                        )
                    )
                    val distance = if (post.distance!! > 1000.0) "${((post.distance / 100).roundToInt().toDouble())/10} km" else "${(post.distance).roundToInt()}m"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Home, contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(id = R.color.green)
                        )
                        Text(
                            text = "내 위치에서 $distance",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
                DrawLine()
                Spacer(modifier = Modifier.height(10.dp))
                ConstraintLayout(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 13.dp, start = 20.dp, end = 17.dp)
                ) {
                    val (nickname, distance) = createRefs()
                    userData?.let {
                        Row(
                            modifier = Modifier.constrainAs(nickname) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    navHostController.navigate("${NAV_ROUTE.TRADE_HISTORY.routeName}/${post.userId!!}")
                                }) {
                                Text(
                                    text = it.nickname,
                                    color = Color.DarkGray,
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                )
                            }
                            Image(
                                painter = painterResource(when(level) {
                                    2 ->
                                        when (userFlower) {
                                            1 -> R.drawable.level2_ver1
                                            2 -> R.drawable.level2_ver2
                                            3 -> R.drawable.level2_ver3
                                            else -> R.drawable.level1
                                        }
                                    3 ->
                                        when(userFlower){
                                            1 -> R.drawable.level3_ver1
                                            2 -> R.drawable.level3_ver2
                                            3 -> R.drawable.level3_ver3
                                            else -> R.drawable.level1
                                        }
                                    4 ->
                                        when(userFlower){
                                            1 -> R.drawable.level4_ver1
                                            2 -> R.drawable.level4_ver2
                                            3 -> R.drawable.level4_ver3
                                            else -> R.drawable.level1
                                        }
                                    else -> R.drawable.level1
                                }),
                                contentDescription = "App icon",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(start = 5.dp)
                            )
                        }
                    }
                    Image(
                        painterResource(id = R.drawable.icon_chat), contentDescription = "",
                        modifier = Modifier.constrainAs(distance){
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }.size(25.dp)
                            .clickable {
                                val contactUserId = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
                                // chatId는 포스트 아이디와 접근한 유저 아이디를 합쳐서 만듬
                                val chatId = post.id.toString() + " " +contactUserId.toString()

                                navHostController.navigate("${NAV_ROUTE.CHAT.routeName}/${chatId}/${post.id!!}")
                            }
                    )

                }

            }
        }
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .background(Color.Transparent)
        )
    }
}

@Composable
fun DrawLine(){
    val color = colorResource(id = R.color.backgroundGray)
    Canvas(modifier = Modifier.fillMaxWidth()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = 2.5f
        )
    }
}
