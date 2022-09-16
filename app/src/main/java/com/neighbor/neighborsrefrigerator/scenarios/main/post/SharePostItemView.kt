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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlin.math.roundToInt
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
fun ItemCardByTime(postViewModel: PostViewModel = viewModel(), post: PostData, route: NAV_ROUTE, navHostController: NavHostController, type: Int){
    var nickname by remember {
        mutableStateOf("")
    }
    var level by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit){
        val userData = postViewModel.getUserData(post.userId)
        level = postViewModel.getUserLevel(post.userId)
        nickname = userData?.nickname?:""
    }

    val current = System.currentTimeMillis()
    val calTime = CalculateTime()
    val time = calTime.calTimeToPost(current, post.createdAt)


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
                    ItemImage(productImg = post.productimg1, modifier = modifier)
                }
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(5.dp))
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (title, button) = createRefs()
                        Column(modifier = Modifier
                            .constrainAs(title) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                            .padding(end = 95.dp)
                        ) {
                                //Spacer(modifier = Modifier.height(10.dp))
                            val distance = if (post.distance!! > 1000.0) "${((post.distance / 100).roundToInt().toDouble())/10} km" else "${(post.distance).roundToInt()}m"
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 5.dp, bottom = 4.dp)) {
                                Icon(
                                    Icons.Filled.Home, contentDescription = "",
                                    modifier = Modifier.size(14.dp),
                                    tint = colorResource(id = R.color.green)
                                )
                                Text(
                                    text = "내 위치에서 $distance",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(start = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = post.title,
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
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
                            .width(80.dp)
                        ){

                            TextButton(
                                onClick = { /*TODO*/ },
                                elevation = ButtonDefaults.elevation(0.dp),
                                colors = if (post.completedAt == null){
                                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green), disabledBackgroundColor = colorResource(id = R.color.green))
                                }else{
                                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.completeGray), disabledBackgroundColor = colorResource(id = R.color.completeGray))
                                },
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.size(80.dp, 33.dp),
                                enabled = false
                            ) {
                                if (post.completedAt == null){
                                    Text(text = "나눔중", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                }else{
                                    Text(text = "나눔 완료", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
            if (type == 1){
                NicknameText(nickname = nickname, time = time)
                DrawLine()
            }

        }
    }

}
@Composable
fun NicknameText(nickname: String, time : String){
    DrawLine()
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, bottom = 5.dp, start = 15.dp, end = 15.dp)) {
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
                        .padding(end = 3.dp)
                )
                Text(text = nickname, color = Color.DarkGray, fontSize = 12.sp)
            }

        }
        Text(text = time, fontSize = 10.sp, color = Color.DarkGray, textAlign = TextAlign.End,
            modifier = Modifier.constrainAs(distanceTextView) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
    }
}

@Composable
fun ItemImage(productImg:String, modifier: Modifier){
    Surface (shape = MaterialTheme.shapes.small.copy(CornerSize(8.dp))){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(productImg)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.icon_google),
            modifier = modifier
        )
    }
}

@Composable
fun IsTrustMark(isTrust: Boolean) {
    if (isTrust) {
        Icon(Icons.Filled.Favorite, tint = Color.Yellow, contentDescription = null, modifier = Modifier
            .size(30.dp)
            .padding(end = 10.dp, top = 10.dp))
    }
}
