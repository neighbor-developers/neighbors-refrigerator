package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
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
import java.util.*
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ProductData
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_DATE_TIME


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardByTime(post: PostData/* onClick: ()-> Unit */,  route: NAV_ROUTE, navHostController: NavHostController){
    Card(
        onClick= {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = "post", value = post)
            navHostController.navigate(route = route.routeName)
         },
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp)),
        elevation = 10.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            Column() {
                val modifier = Modifier.fillMaxHeight(0.6f)
                post.productImg?.let {
                    ItemImage(productImg = post.productImg, modifier = modifier)
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
        elevation = 10.dp
    ) {
        Surface(shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp))) {
            Row(verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    val modifier = Modifier.size(100.dp)
                    post.productImg?.let {
                        ItemImage(productImg = post.productImg, modifier = modifier)
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

    val postTime = post.validateDate
    var token = postTime!!.split("T")[0].split("-")

    val day = "${token[0]}년 ${token[1]}월 ${token[2]}일"
    val validateType = when (post.validateType) {
        1 -> "유통기한"
        2 -> "제조일자"
        3 -> "구매일자"
        else -> { "" }
    }
    Column(Modifier.padding(10.dp)) {
        Text(text = post.title, fontSize = 15.sp, color = Color.Black, modifier = Modifier.padding(bottom = 10.dp))
        Text(text = "$validateType : $day", fontSize = 10.sp, color = Color.Black)
        post.distance?.let {
            Text(text = "내 위치에서 ${post.distance}km", fontSize = 10.sp, color = Color.Black)
        }
        Text(text = "업로드 : 3분전", fontSize = 10.sp, color = Color.Black)
    }
}
@Composable
fun ItemImage(productImg:String, modifier: Modifier){
    Surface (shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp))){
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
//
//@Preview
//@Composable
//fun ItemViewPreview() {
//    ItemCardByTime(
//        ProductIncludeDistanceData(
//        ProductData(
//            productID = ",",
//            postID = 1,
//            productName = "a",
//            validateType = 1,
//            validateDate = Date(2022, 3, 4),
//            validateImg = null,
//            productImg = "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F7a2ccc0b-6765-4972-8221-8310ba6575a6%2FUntitled.png?table=block&id=9b25b556-a9a7-4637-a960-fd87f7b72cab&spaceId=cd12198d-d450-4deb-b809-0aa7cc554553&width=1730&userId=5c9e2a77-0418-4987-b276-3de00770167c&cache=v2"
//        ),
//        distance = 3.4)
//    )
//}
