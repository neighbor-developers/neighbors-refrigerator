package com.neighbor.neighborsrefrigerator.scenarios.main.post.detail

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.ChatViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel

@Composable
fun SeekPostDetailScreen(navHostController: NavHostController, postViewModel: PostViewModel = viewModel(), post: PostData) {

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "구함 상세 페이지", fontSize = 30.sp)
            Text(text = "제목 : ${post.title}")
            Button(onClick = { navHostController.navigateUp() }) {
                Text(text = "뒤로가기", Modifier.size(width = 100.dp, height = 40.dp))
            }
        }

        Button(onClick = {

            val contactUserId = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
            // chatId는 포스트 아이디와 접근한 유저 아이디를 합쳐서 만듬
            val chatId = post.id.toString() + contactUserId.toString()

            navHostController.navigate("${NAV_ROUTE.CHAT.routeName}/${chatId}/${post.id!!}")
        }){
            Text(text = "채팅하기")
        }
    }
}
