package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.ProductData
import com.neighbor.neighborsrefrigerator.scenarios.main.RouteAction

@Composable
fun SharePostDetail(routeAction: RouteAction, productID: String?) {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "나눔 상세 페이지", fontSize = 30.sp)
            Text(text = "상품 아이디 : $productID!!")
            Button(onClick = { routeAction.goBack() }) {
                Text(text = "뒤로가기", Modifier.size(width = 100.dp, height = 40.dp))
            }
        }
    }
}