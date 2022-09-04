package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardByTime(post: PostData/* onClick: ()-> Unit */,  route: NAV_ROUTE, navHostController: NavHostController){
    Card(
        onClick= {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = post)
            navHostController.navigate(route = route.routeName)
         },
        shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp)),
        elevation = 0.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            Column() {
                val modifier = Modifier.aspectRatio(1f)
                post.productimg1?.let {
                    ItemImage(productimg1 = post.productimg1, modifier = modifier)
                }

                ItemText(post = post)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd)
            {
                IsTrustMark(isTrust = !post.validateImg.isNullOrEmpty())
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardByDistance(post: PostData, route: NAV_ROUTE, navHostController: NavHostController) {
    Card(
        onClick= {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = post)
            navHostController.navigate(route = route.routeName) },

        modifier = Modifier
            .padding(end = 20.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp)),
        elevation = 0.dp
    ) {
        Surface(shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp))) {
            Row(verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    val modifier = Modifier.aspectRatio(1f)
                    post.productimg1?.let {
                        ItemImage(productimg1 = post.productimg1, modifier = modifier)
                    }

                }
                Box(contentAlignment = Alignment.TopEnd) {
                    IsTrustMark(isTrust = !post.validateImg.isNullOrEmpty())
                    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Bottom, modifier = Modifier
                        .height(100.dp)) {
                        ItemText(post = post)
                    }
                }
            }
        }
    }
}
@Composable
fun ItemText(post: PostData){
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

//    val token = post.validateDate!!.split("T")[0].split("-")
//
//    val validateDate = "${token[0]}년 ${token[1]}월 ${token[2]}일"
//    val validateType = when (post.validateType) {
//        1 -> "유통기한"
//        2 -> "제조일자"
//        3 -> "구매일자"
//        else -> { "" }
//    }
    Column(Modifier.padding(10.dp)) {
        Text(text = post.categoryId)
        Text(text = post.title, fontSize = 18.sp, color = Color.Black, modifier = Modifier.padding(bottom = 7.dp))
        //Text(text = "$validateType : $validateDate", fontSize = 10.sp, color = Color.Black)
//        distancePost?.let {
//            distancePost?.let {
//                val distanceText = "${(it / 10).roundToInt() / 100} km"
//                Text(text = "내 위치에서 $distanceText", fontSize = 10.sp, color = Color.DarkGray)
//            }
//        }

        Text(text = "업로드 : $time", fontSize = 10.sp, color = Color.Black)
    }
}
@Composable
fun ItemImage(productimg1:String, modifier: Modifier){
    Surface (shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp))){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(productimg1)
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
