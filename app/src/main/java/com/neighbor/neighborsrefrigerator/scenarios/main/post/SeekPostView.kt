package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
    postViewModel: PostViewModel ,
    route: NAV_ROUTE,
    navHostController: NavHostController
) {
    Column {
//        Text(
//            text = "# 이웃 주민과 함께 어쩌구 ~",
//            modifier = Modifier.padding(start = 30.dp, end = 15.dp, top = 30.dp, bottom = 10.dp),
//            fontSize = 20.sp
//        )
        SeekPostList(postViewModel.seekPostsByTime.collectAsLazyPagingItems(), route = route, navHostController = navHostController, viewModel = postViewModel)
    }
}
@Composable
fun SeekPostList(posts: LazyPagingItems<PostData>,
                 route: NAV_ROUTE,
                 navHostController: NavHostController,
                 viewModel: PostViewModel
){
    val scrollState = LazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = scrollState
    ){
        items(posts){
            if (it != null) {
                SeekItem(post = it, route = route, navHostController = navHostController, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SeekItem(post: PostData,
             route: NAV_ROUTE,
             navHostController: NavHostController,
             viewModel: PostViewModel){

    var userData: UserData? by remember {
        mutableStateOf(null)
    }

    val calDistance = CalDistance()
    val lat  by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble())}
    val lng  by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble())}

    val distancePost  by remember{
        mutableStateOf(
        if(lat != null && lng !=null){
        calDistance.getDistance(lat!!, lng!!, post.latitude,post.longitude)
    }else null)}

    val current = System.currentTimeMillis()
    val calTime = CalculateTime()
    val time = calTime.calTimeToPost(current, post.createdAt)

    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.Main).launch {
            userData = viewModel.getUserNickname(post.userId)

        }
    }

    val color : Color = colorResource(id = R.color.green)

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp)){
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = 3f
        )
    }
    Card(
        onClick = {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = post)
            navHostController.navigate(route = route.routeName)
        },
        modifier = Modifier
            .padding(top = 15.dp, bottom = 15.dp, start = 30.dp, end = 30.dp)
            .fillMaxWidth(), elevation = 0.dp) {
        Column(Modifier.fillMaxWidth()) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (title, timeText) = createRefs()
                Text(text = post.title, fontSize = 15.sp,
                    modifier = Modifier.constrainAs(title){
                        start.linkTo(parent.start)
                    }
                )
                Text(text = time, fontSize = 10.sp, color = Color.DarkGray,
                    modifier = Modifier.constrainAs(timeText){
                        end.linkTo(parent.end)
                    }
                )
            }
            Row {
                Spacer(modifier = Modifier.width(7.dp))
                Text(text = post.content, fontSize = 12.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
            }
            Canvas(modifier = Modifier.fillMaxWidth()){
                val canvasWidth = size.width
                val canvasHeight = size.height

                drawLine(
                    start = Offset(x = 0f, y = canvasHeight),
                    end = Offset(x = canvasWidth, y = canvasHeight),
                    color = color,
                    strokeWidth = 3f
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (nickname, distance) = createRefs()
                userData?.let {
                    Row(modifier = Modifier.constrainAs(nickname){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)                            },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = it.nickname, color = Color.DarkGray, fontSize = 12.sp)
                        Image(
                            painter = painterResource(R.drawable.sprout),
                            contentDescription = "App icon",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(20.dp)
                                .padding(start = 5.dp)
                        )
                    }
                }
                distancePost?.let {
                    val distanceText = "${(it / 10).roundToInt() /100} km"
                    Text(text = "내 위치에서 $distanceText", fontSize = 10.sp, color = Color.DarkGray,
                        modifier = Modifier.constrainAs(distance){
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    )

                }


            }

        }
    }
    Canvas(modifier = Modifier.fillMaxWidth()){
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = 3f
        )
    }
}
