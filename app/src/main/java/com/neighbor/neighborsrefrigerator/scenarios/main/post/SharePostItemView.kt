package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.data.ProductData
import java.util.*
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import java.util.concurrent.CountDownLatch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardByTime(product: ProductIncludeDistanceData/* onClick: ()-> Unit */,  route: NAV_ROUTE, navHostController: NavHostController) {
    Card(
        onClick= { navHostController.navigate(route = "${route.routeName}/${product.productData.id}") },
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(20.dp)),
        elevation = 10.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            Column() {
                val modifier = Modifier.fillMaxHeight(0.6f)
                ItemImage(productImg = product.productData.productImg, modifier = modifier)
                ItemText(product = product.productData, product.distance)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd)
            {
                IsTrustMark(isTrust = !product.productData.validateImg.isNullOrEmpty())
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardByDistance(product: ProductIncludeDistanceData, route: NAV_ROUTE, navHostController: NavHostController) {
    Card(
        onClick= { navHostController.navigate("${route.routeName}/${product.productData.id}") },
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
                    ItemImage(productImg = product.productData.productImg, modifier = modifier)

                }
                Box(contentAlignment = Alignment.TopEnd) {
                    IsTrustMark(isTrust = !product.productData.validateImg.isNullOrEmpty())
                    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Bottom, modifier = Modifier
                        .height(100.dp)) {
                        ItemText(product = product.productData, distance = product.distance)
                    }
                }
            }
        }
    }
}
@Composable
fun ItemText(product: ProductData, distance: Double){
    val day = "${product.validateDate.year}년 ${product.validateDate.month}월 ${product.validateDate.day}일"
    val valiType = when (product.validateType) {
        1 -> "유통기한"
        2 -> "제조일자"
        3 -> "구매일자"
        else -> { "" }
    }
    Column(Modifier.padding(10.dp)) {
        Text(text = product.productName, fontSize = 15.sp, color = Color.Black, modifier = Modifier.padding(bottom = 10.dp))
        Text(text = "$valiType : $day", fontSize = 10.sp, color = Color.Black)
        Text(text = "내 위치에서 ${distance}km", fontSize = 10.sp, color = Color.Black)
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
