package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SeekPostDetail(navHostController: NavHostController, postId: String?) {

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "구함 상세 페이지", fontSize = 30.sp)
            Text(text = "제목 : $postId")
            Button(onClick = { navHostController.navigateUp() }) {
                Text(text = "뒤로가기", Modifier.size(width = 100.dp, height = 40.dp))
            }
        }
    }
}