package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData

@Composable
fun SharePostDetail(navHostController: NavHostController, product: String?) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { /*신고 다이얼로그*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "")
                    }
                },
                navigationIcon = {
                    IconButton(onClick =  { navHostController.navigateUp() }) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxSize()

            )
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {

        }
        Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            Text(text = "상품 자세히 보기", fontSize = 20.sp)
            Row() {
                val modifier = Modifier.size(200.dp)
//                ItemImage(productImg = product.productData.productImg, modifier = modifier)
//
//                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                    val day = "${product.productData.validateDate.year}년 ${product.productData.validateDate.month}월 ${product.productData.validateDate.day}일"
//                    val valiType = when (product.productData.validateType) {
//                        1 -> "유통기한"
//                        2 -> "제조일자"
//                        3 -> "구매일자"
//                        else -> { "" }
//                    }
//                    Text(text = product.productData.productName, fontSize = 15.sp, color = Color.White, modifier = Modifier.padding(bottom = 10.dp))
//                    Text(text = "$valiType : $day", fontSize = 10.sp, color = Color.White)
//                    Text(text = "내 위치에서 ${product.distance}km", fontSize = 10.sp, color = Color.White)
//                    Text(text = "업로드 : 3분전", fontSize = 10.sp, color = Color.White)
//                    }
                }
            Text(text = "상품 상세 설명", fontSize = 20.sp)
            Text(text = "어ㅉ거구", modifier = Modifier.fillMaxWidth().height(200.dp))
            Text(text = "상품 사진 모아보기", fontSize = 20.sp)

        }

    }
}



@Preview
@Composable
fun preview111(){

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "나눔 상세 페이지", fontSize = 30.sp)
            Text(text = "상품 아이디 : h")
            Button(onClick = { }) {
                Text(text = "뒤로가기", Modifier.size(width = 100.dp, height = 40.dp))
            }
        }
    }
}