package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.data.ProductData
import java.util.*
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData

@Composable
fun ItemCardByTime(product: ProductIncludeDistanceData/* onClick: ()-> Unit */) {
    Card(
        // onClick= onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {

                val modifier = Modifier.fillMaxWidth()
                ItemImage(productImg = product.productData.productImg, modifier = modifier)
                IsTrustMark(isTrust = !product.productData.validateImg.isNullOrEmpty())
            }
           ItemText(product = product.productData, product.distance)
        }
    }
}

@Composable
fun ItemCardByDistance(product: ProductIncludeDistanceData/* onClick: ()-> Unit */) {
    Card(
        // onClick= onClick,
        modifier = Modifier.padding(end = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Box( modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {

                val modifier = Modifier.height(100.dp).padding(end = 10.dp)
                ItemImage(productImg = product.productData.productImg, modifier = modifier)
                IsTrustMark(isTrust = !product.productData.validateImg.isNullOrEmpty())
            }
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(bottom = 10.dp)) {
                ItemText(product = product.productData, distance = product.distance)
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
    Text(text = product.productName, fontSize = 17.sp, modifier = Modifier.padding(bottom = 10.dp))
    Text(text = "$valiType : $day", fontSize = 12.sp, color = Color.DarkGray)
    Text(text = "내 위치에서 ${distance}km", fontSize = 12.sp, color = Color.DarkGray)
    Text(text = "업로드 : 3분전", fontSize = 12.sp, color = Color.DarkGray)
}
@Composable
fun ItemImage(productImg:String, modifier: Modifier){
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



@Composable
fun IsTrustMark(isTrust: Boolean) {
    if (isTrust) {
        Icon(Icons.Filled.Favorite, tint = Color.Yellow, contentDescription = null, modifier = Modifier
            .size(30.dp)
            .padding(5.dp))
    }
}

@Preview
@Composable
fun ItemViewPreview() {
    ItemCardByTime(
        ProductIncludeDistanceData(
        ProductData(
            productID = ",",
            postID = 1,
            productName = "a",
            validateType = 1,
            validateDate = Date(2022, 3, 4),
            validateImg = null,
            productImg = "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F7a2ccc0b-6765-4972-8221-8310ba6575a6%2FUntitled.png?table=block&id=9b25b556-a9a7-4637-a960-fd87f7b72cab&spaceId=cd12198d-d450-4deb-b809-0aa7cc554553&width=1730&userId=5c9e2a77-0418-4987-b276-3de00770167c&cache=v2"
        ),
        distance = 3.4)
    )
}
