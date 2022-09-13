package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
import com.neighbor.neighborsrefrigerator.utilities.CalculateTime
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardByTime(postViewModel: PostViewModel = viewModel(), post: PostData/* onClick: ()-> Unit */,  route: NAV_ROUTE, navHostController: NavHostController){
    var nickname by remember {
        mutableStateOf("")
    }
    var level by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit){
        val userData = postViewModel.getUserNickname(post.userId)
        nickname = userData?.nickname?:""
        //level = userData?.score?:0
    }

    val current = System.currentTimeMillis()
    val calTime = CalculateTime()
    val time = calTime.calTimeToPost(current, post.createdAt)

    val calDistance = CalDistance()
    val lat  by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble())
    }
    val lng  by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble())
    }

    val distancePost  by remember{
        mutableStateOf(
            if(lat != null && lng !=null){
                calDistance.getDistance(lat!!, lng!!, post.latitude,post.longitude)
            }else null)
    }

    Card(
        onClick= {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = post)
            navHostController.navigate(route = route.routeName)
         },
        modifier = Modifier.background(Color.White), elevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                val modifier = Modifier
                    .height(80.dp)
                    .aspectRatio(1f)
                post.productimg1?.let {
                    ItemImage(productImg1 = post.productimg1, modifier = modifier)
                }
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth()) {
                    distancePost?.let {
                        //Spacer(modifier = Modifier.height(10.dp))
                        val distanceText = "${(it / 10).roundToInt() / 100} km"
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 5.dp, bottom = 4.dp)) {
                            Icon(
                                Icons.Filled.Home, contentDescription = "",
                                modifier = Modifier.size(14.dp),
                                tint = colorResource(id = R.color.green)
                            )
                            Text(
                                text = "내 위치에서 $distanceText",
                                fontSize = 11.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (title, button) = createRefs()
                        Column(modifier = Modifier
                            .constrainAs(title) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                            .padding(end = 95.dp)) {
                            Text(
                                text = "고구마랑 대파 나눔해용",
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = post.content,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }

                        Box(modifier = Modifier
                            .constrainAs(button) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .fillMaxHeight()
                            .width(80.dp)
                        ){

                            TextButton(
                                onClick = { /*TODO*/ },
                                elevation = ButtonDefaults.elevation(0.dp),
                                colors = if (post.review == null){
                                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green), disabledBackgroundColor = colorResource(id = R.color.green))
                                }else{
                                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.completeGray), disabledBackgroundColor = colorResource(id = R.color.completeGray))
                                },
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.size(80.dp, 33.dp),
                                enabled = false
                            ) {
                                if (post.review == null){
                                    Text(text = "나눔중", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                }else{
                                    Text(text = "나눔 완료", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
            DrawLine()
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 8.dp)) {
                val (nicknameTextView, distanceTextView) = createRefs()
                if (nickname != "") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.constrainAs(nicknameTextView){
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.level1),
                            contentDescription = "App icon",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(20.dp)
                                .padding(end = 5.dp)
                        )
                        Text(text = nickname, color = Color.DarkGray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Text(text = time, fontSize = 10.sp, color = Color.DarkGray, textAlign = TextAlign.End,
                    modifier = Modifier.constrainAs(distanceTextView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })

            }
        }
    }
    Spacer(modifier = Modifier
        .height(15.dp)
        .background(Color.Transparent))
}

@Composable
fun ItemImage(productImg1:String, modifier: Modifier){
    Surface (shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp))){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(productImg1)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.icon_google),
            modifier = modifier
        )
    }
}

//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ItemCardByDistance(post: PostData, route: NAV_ROUTE, navHostController: NavHostController) {
//    Card(
//        onClick= {
//            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = post)
//            navHostController.navigate(route = route.routeName) },
//
//        modifier = Modifier
//            .padding(end = 20.dp)
//            .fillMaxWidth(),
//        shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp)),
//        elevation = 0.dp
//    ) {
//        Surface(shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp))) {
//            Row(verticalAlignment = Alignment.Bottom) {
//                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
//                    val modifier = Modifier.aspectRatio(1f)
//                    post.productimg1?.let {
//                        ItemImage(productimg1 = post.productimg1, modifier = modifier)
//                    }
//
//                }
//                Box(contentAlignment = Alignment.TopEnd) {
//                    IsTrustMark(isTrust = !post.validateImg.isNullOrEmpty())
//                    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Bottom, modifier = Modifier
//                        .height(100.dp)) {
//                        ItemText(post = post)
//                    }
//                }
//            }
//        }
//    }
//}
@Composable
fun IsTrustMark(isTrust: Boolean) {
    if (isTrust) {
        Icon(Icons.Filled.Favorite, tint = Color.Yellow, contentDescription = null, modifier = Modifier
            .size(30.dp)
            .padding(end = 10.dp, top = 10.dp))
    }
}
@Composable
@Preview
fun A(){
    val post = PostData(id=257, title="제목246", categoryId= "100", userId=1, content="내용246", type=1, mainAddr="경기도시흥시246", addrDetail="246호", rate=0, review=null, validateType=1, validateDate="2022-08-22T0000:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null)
    ItemCardByTime(post = post, route = NAV_ROUTE.SHARE_DETAIL, navHostController = NavHostController(LocalContext.current))
}
