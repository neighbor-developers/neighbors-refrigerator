package com.neighbor.neighborsrefrigerator.scenarios.main.post.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.scenarios.main.ShowUserProfile
import com.neighbor.neighborsrefrigerator.scenarios.main.post.ItemImage
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
import com.neighbor.neighborsrefrigerator.utilities.CalculateTime
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


@Composable
fun SharePostDetailScreen(
    navHostController: NavHostController,
    postViewModel: PostViewModel = viewModel(),
    post: PostData
) {

    val imageLoadState = remember {
        mutableStateOf(false)
    }
    val shadowColor = colorResource(id = R.color.shadowGray)

    var nickname by remember {
        mutableStateOf("")
    }
    var level by remember {
        mutableStateOf(0)
    }
    val userId = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
    LaunchedEffect(Unit) {
        val userData = postViewModel.getUserData(post.userId)
        level = postViewModel.getUserLevel(userId)
        nickname = userData?.nickname ?: ""
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
                        Icon(
                            painterResource(id = R.drawable.icon_decl_color),
                            contentDescription = "신고하기",
                            modifier = Modifier.size(25.dp),
                            tint = colorResource(
                                id = R.color.declRed
                            )
                        )
                        Icon(
                            painterResource(id = R.drawable.icon_decl),
                            contentDescription = "신고하기",
                            modifier = Modifier.size(25.dp),
                            tint = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            painterResource(id = R.drawable.icon_back),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(35.dp),
                            tint = colorResource(
                                id = R.color.green
                            )
                        )
                    }
                },
                backgroundColor = if (imageLoadState.value) shadowColor else Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (declarationDialogState) {
                DeclarationDialog(postId = post.id!!, type = 1, onChangeState = {
                    declarationDialogState = false
                })
            }

            PostDataScreen(
                navHostController = navHostController,
                postViewModel = postViewModel,
                post = post,
                nickname = nickname,
                level = level,
                imageLoadState
            )
        }
    }
}

@Composable
fun PostDataScreen(
    navHostController: NavHostController,
    postViewModel: PostViewModel,
    post: PostData,
    nickname: String,
    level: Int,
    imageLoadState : MutableState<Boolean>
) {
    val current = System.currentTimeMillis()
    val calTime = CalculateTime()
    val createdTime = calTime.calTimeToPost(current, post.createdAt)

//    val calDistance = CalDistance()
//    val lat by remember {
//        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble())
//    }
//    val lng by remember {
//        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble())
//    }
//

    val distance = if (post.distance!! > 1000.0) "${((post.distance / 100).roundToInt().toDouble())/10} km" else "${post.distance}m"
    val contactUserId by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt())
    }

    val validateDate = post.validateDate?.let {
        val date = it.split(".")[0].replace("T", " ")
        val formattedTime = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA).parse(date)?.time
        SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(formattedTime)
    }

    val validateType = when (post.validateType) {
        1 -> "유통기한"
        2 -> "제조일자"
        3 -> "구매일자"
        else -> {
            ""
        }
    }

    var titleFontSize by remember {
        mutableStateOf(18.sp)
    }

    var expandedImage by remember {
        mutableStateOf(post.productimg1)
    }

    var completeShareDialog by remember {
        mutableStateOf(false)
    }
    val userFlower = UserSharedPreference(App.context()).getLevelPref("flowerVer")

    if (completeShareDialog) {
        post.let {
            CompleteDialog(
                type = "거래",
                { completeShareDialog = false },
                { postViewModel.completeShare(it) }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (main, user) = createRefs()
            Column(
                modifier = Modifier
                    .padding(start = 27.dp, end = 27.dp, bottom = 130.dp)
                    .constrainAs(main) {
                        top.linkTo(parent.top)
                    }
                    .verticalScroll(
                        enabled = true,
                        state = rememberScrollState()
                    )
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "나눔 자세히 보기",
                    fontSize = 20.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(35.dp))
                Row() {
                    post.productimg1?.let {
                        val modifier = Modifier.size(120.dp).clickable {
                            imageLoadState.value = true
                            expandedImage = it
                        }
                        ItemImage(productImg = post.productimg1, modifier = modifier)
                    }

                    Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                        Text(text = post.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = titleFontSize,
                            color = Color.Black,
                            modifier = Modifier.height(75.dp),
                            onTextLayout = {
                                titleFontSize = if(it.lineCount == 1){
                                    21.sp
                                } else if (it.lineCount < 3)
                                    19.sp
                                else if (it.lineCount == 3)
                                    16.sp
                                else
                                    13.3.sp
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = distance,
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$validateType : $validateDate",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (text, button) = createRefs()
                    Text(text = "상세 설명",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.constrainAs(text) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )
                    TextButton(
                        onClick = { /*TODO*/ },
                        elevation = ButtonDefaults.elevation(0.dp),
                        colors = if (post.completedAt == null) {
                            ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.green),
                                disabledBackgroundColor = colorResource(id = R.color.green)
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(id = R.color.completeGray),
                                disabledBackgroundColor = colorResource(id = R.color.completeGray)
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(90.dp, 37.dp)
                            .constrainAs(button) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                        enabled = false
                    ) {
                        if (post.completedAt == null) {
                            Text(
                                text = "나눔중",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = "나눔 완료",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Text(
                    text = post.content,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 25.dp)
                )
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "업로드 : $createdTime",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )

                }

                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "사진",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    post.productimg1?.let {
                        ItemImage(productImg = it, modifier = Modifier
                            .size(85.dp)
                            .clickable {
                                imageLoadState.value = true
                                expandedImage = it
                            })
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    post.productimg2?.let {
                        ItemImage(productImg = it, modifier = Modifier
                            .size(85.dp)
                            .clickable {
                                imageLoadState.value = true
                                expandedImage = it
                            })
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    post.productimg3?.let {
                        ItemImage(productImg = it, modifier = Modifier
                            .size(85.dp)
                            .clickable {
                                imageLoadState.value = true
                                expandedImage = it
                            })
                    }
                }
            }
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(user) {
                        bottom.linkTo(parent.bottom)
                    }
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(topEnd = 60.dp))
                    .background(
                        shape = RoundedCornerShape(topEnd = 60.dp),
                        color = colorResource(id = R.color.green)
                    )
                    .height(130.dp)
                    .padding(20.dp)
            ) {
                val (userData, button) = createRefs()
                Column(modifier = Modifier.constrainAs(userData) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                    if (nickname != "") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp).clickable {
                                navHostController.navigate("${NAV_ROUTE.TRADE_HISTORY.routeName}/${post.userId}")
                            }
                        ) {

                            Surface(modifier = Modifier.size(28.dp),
                                contentColor = Color.White,
                                color = Color.White,
                                shape = CircleShape,
                                content = {
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
                                            .size(25.dp)
                                            .padding(4.dp)
                                            .background(color = Color.White, shape = CircleShape)
                                    )
                                }
                            )
                            Text(
                                text = nickname,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                        }

                    }
                }
                TextButton(
                    onClick = {
                        if (post.userId != contactUserId) {
                            val chatId = post.id.toString() + " " + contactUserId.toString()
                            navHostController.navigate("${NAV_ROUTE.CHAT.routeName}/${chatId}/${post.id!!}")
                        } else {
                            completeShareDialog = true
                        }
                    },
                    elevation = ButtonDefaults.elevation(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        disabledBackgroundColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .size(90.dp, 37.dp)
                        .constrainAs(button) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    enabled = true
                ) {
                    Text(
                        text = if (post.userId == contactUserId) "나눔 완료" else "채팅하기",
                        color = colorResource(id = R.color.green),
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }
        if (imageLoadState.value){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        imageLoadState.value = false
                    }
                    .background(color = colorResource(id = R.color.shadowGray))) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(expandedImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.icon_google),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)

                )
            }
        }
    }
}


@Preview
@Composable
fun P() {
    SharePostDetailScreen(
        navHostController = NavHostController(LocalContext.current), post =
        PostData(
            id = 312,
            title = "명절 선물로 받은 사과 나눔합니당! ",
            categoryId = "200",
            userId = 2,
            content = "이번 추석 선물로 사과 두박스가 들어와서 몇개 나눔할게요:)\n위치는 정왕동입니다! 채팅 주세요이번 추석 선물로 사과 두박스가 들어와서 몇개 나눔할게요:)",
            type = 2,
            mainAddr = "경기도시흥시301",
            addrDetail = "301호",
            rate = 0,
            review = null,
            validateType = 1,
            validateDate = "2022-08-22T00:00:00.000Z",
            validateImg = null,
            productimg1 = "https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg",
            productimg2 = null,
            productimg3 = null,
            createdAt = "2022-08-22T16:45:04.000Z",
            updatedAt = "2022-08-22T16:47:28.000Z",
            completedAt = null,
            latitude = 37.34,
            longitude = 126.73,
            state = "1",
            distance = null
        )
    )
}
