package com.neighbor.neighborsrefrigerator.scenarios.main.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.data.ProductData
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.ItemImage
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog
import java.util.*

@Composable
fun SharePostDetail(navHostController: NavHostController, productID: String?) {

    // val post = PostData(1, "헬로미스터 마이 예스터데이", "100_10", 1, "어쩌고저쩌고어쩌고저쩌고쩌고저쩌고어쩌고저쩌고쩌고저쩌고어쩌고저쩌고~~~~~~~~~~~~~~~~₩", 1, "ㅇㅇ", 0.4, "!!!!", Date(2022, 3, 4), Date(2022, 3, 4), Date(2022, 3, 4))
    val product = ProductIncludeDistanceData(
        ProductData(1, 1, "감자랑 고구마", 1, Date(2022, 3, 4), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://mediahub.seoul.go.kr/wp-content/uploads/2016/09/61a2981f41200ac8c513a3cbc0010efe.jpg"), 3.4)

    val day = "${product.productData.validateDate.year}년 ${product.productData.validateDate.month}월 ${product.productData.validateDate.day}일"
    val valiType = when (product.productData.validateType) {
        1 -> "유통기한"
        2 -> "제조일자"
        3 -> "구매일자"
        else -> {
            ""
        }
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
                        Icon(Icons.Filled.Warning, contentDescription = "", tint = Color.Red)
                    }
                },
                navigationIcon = {
                    IconButton(onClick =  { navHostController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            if (declarationDialogState) {
                DeclarationDialog(type = 1) {
                    declarationDialogState = false
                }
            }
            Column {
                Text(text = "상품 자세히 보기", fontSize = 20.sp, modifier = Modifier.padding(start = 30.dp, top = 10.dp, bottom = 20.dp))
                Row() {
                    Spacer(modifier = Modifier.width(30.dp))
                    val modifier = Modifier.size(150.dp)
                    ItemImage(productImg = product.productData.productImg, modifier = modifier)
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = product.productData.productName, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(30.dp))
                Surface(shape = MaterialTheme.shapes.large.copy(topEnd = CornerSize(70.dp))) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xff00ac77))) {
                        Text(text = "내 위치에서 ${product.distance}km", fontSize = 20.sp, color = Color.White)
                        Text(text = "$valiType : $day", fontSize = 20.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(){

}



@Preview
@Composable
fun Rreview111() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Warning, contentDescription = "")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {

        }
    }
}