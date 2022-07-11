package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostViewModel

@Composable
fun SeekPostScreen(
    sharePostViewModel: SharePostViewModel
) {
    Column() {
        SearchBox(onSearch = { sharePostViewModel.search() })
        Text(
            text = "# 이웃 주민과 함께 어쩌구 ~",
            modifier = Modifier.padding(start = 30.dp, end = 15.dp, top = 30.dp, bottom = 10.dp),
            fontSize = 15.sp
        )
        SeekPostList(sharePostViewModel.posts.collectAsState())
    }
}
@Composable
fun SeekPostList(posts : State<List<PostData>?>){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp)) {
        posts.value?.let {
            it.forEach {
                SeekItem(postData = it, distance = 3.4)
            }
        }
    }
}

@Composable
fun SeekItem(postData: PostData, distance : Double){
    Card(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
        Text(text = postData.title!!, fontSize = 20.sp, modifier = Modifier.padding(bottom = 10.dp))
        Text(text = postData.content!!, fontSize = 15.sp, color = Color.DarkGray)
        Text(text = "내 위치에서 ${distance}km", fontSize = 12.sp, color = Color.DarkGray)
        Text(text = "업로드 : 3분전", fontSize = 12.sp, color = Color.DarkGray)
    }
}

@Preview
@Composable
fun Preview22() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Text(
            text = "# 이웃 주민과 함께 어쩌구 ~",
            modifier = Modifier.padding(start = 30.dp, end = 15.dp, top = 30.dp, bottom = 10.dp),
            fontSize = 15.sp
        )
        var content by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, end = 30.dp, start = 30.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            val cornersize = CornerSize(20.dp)
            OutlinedTextField(value = content, onValueChange = { content = it }, modifier = Modifier
                .fillMaxWidth()
                .size(45.dp), shape = MaterialTheme.shapes.large.copy(cornersize))

            IconButton(onClick = {  }) {
                Icon(Icons.Filled.Search, contentDescription = null)
            }
        }

    }
}
