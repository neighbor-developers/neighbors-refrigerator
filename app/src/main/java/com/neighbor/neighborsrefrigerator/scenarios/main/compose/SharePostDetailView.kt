package com.neighbor.neighborsrefrigerator.scenarios.main.compose // ktlint-disable filename

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neighbor.neighborsrefrigerator.scenarios.intro.Greeting
import com.neighbor.neighborsrefrigerator.scenarios.intro.SignInGoogleButton
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostDetailViewModel

data class SharePostDetailsCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: () -> Unit
)

@Composable
fun SharePostDetailsScreen(
    sharePostDetailViewModel: SharePostDetailViewModel,
    onBackClick: () -> Unit, // 뒤로가기
    onAccuseClick: () -> Unit, // 신고
    onChatClick: () -> Unit // 채팅하기
) {
    Surface() {
        
    }
}

@Composable
fun Greeting(text: String, ) {
    Text(text = text, style = MaterialTheme.typography.body2, color = Color.Gray)
}

@Preview
@Composable
fun Preview() {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 13.dp, end = 13.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(text = "시작하시겠습니까?")
            SignInGoogleButton(onClick = { })
        }
    }
}
