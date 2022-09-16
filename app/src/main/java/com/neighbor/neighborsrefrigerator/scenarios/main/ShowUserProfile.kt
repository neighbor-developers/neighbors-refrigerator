package com.neighbor.neighborsrefrigerator.scenarios.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.ReviewData
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ShowUserProfile(navController: NavHostController, userId: Int) {
    var dbAccessModule by remember {
        mutableStateOf(DBAccessModule())
    }
    var postData : ArrayList<PostData> by remember {
        mutableStateOf(arrayListOf())
    }
    dbAccessModule.getPostByUserId(userId){postData = it}

    var userData : UserData? by  remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.Main).launch {
            if (userId != 0) {
                userData = dbAccessModule.getUserInfoById(userId)[0]
            }
        }
    }

    userData?.let {
        userDataScreen(userData = it, navController = navController, postData)
    }
}

@Composable
fun userDataScreen(userData: UserData, navController: NavHostController, postData: ArrayList<PostData>){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val postCount = postData.count{true}
        val calLevel = CalLevel()
        val level = calLevel.GetUserLevel(postData)
        val userId : Int = userData.id!!
        val userFlower = UserSharedPreference(App.context()).getLevelPref("flowerVer")

        var reviewData : ArrayList<ReviewData> by remember {
            mutableStateOf(arrayListOf())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .padding(30.dp, 20.dp, 15.dp, 20.dp)
            ) {
                Image(
                    painter = painterResource(
                        when(level) {
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
                        }
                    ),
                    contentDescription = "profileImage",
                    modifier = Modifier
                        .size(120.dp)
                        .border(2.dp, Color.LightGray, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))

                )
                Text(
                    text = userData.nickname,
                    style = MaterialTheme.typography.overline,
                    color = Color.DarkGray,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(30.dp,0.dp, 0.dp, 0.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Text(
                text = "거래 횟수: $postCount",
                style = MaterialTheme.typography.overline,
                color = Color.DarkGray,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(30.dp, 0.dp, 0.dp, 20.dp)
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
            .fillMaxHeight()
            .padding(30.dp, 8.dp, 30.dp, 8.dp),
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
                modifier = Modifier.align(Alignment.CenterHorizontally)
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
            .border(border = BorderStroke(width = 1.dp, Color.LightGray))
            .fillMaxHeight()
            .padding(30.dp, 8.dp, 30.dp, 8.dp),
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
                modifier = Modifier.align(Alignment.CenterHorizontally)
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