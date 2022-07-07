package com.neighbor.neighborsrefrigerator.scenarios.main.compose // ktlint-disable filename

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostDetailViewModel

data class SharePostDetailsCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: () -> Unit,
    val onChatClick: () -> Unit
)

@Composable
fun SharePostDetailsScreen(
    sharePostDetailViewModel: SharePostDetailViewModel,
    onBackClick: () -> Unit, // 뒤로가기
    onAccuseClick: () -> Unit, // 신고
    onChatClick: () -> Unit // 채팅하기
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Green) {
        Greeting(text = "나눔페이지")
    }
}

@Composable
fun Greeting(text: String) {
    Text(text = text, fontSize = 30.sp)
}

@Preview
@Composable
fun Preview() {
    Surface(color = Color.White) {
    }
}
