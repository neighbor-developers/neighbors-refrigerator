package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel

@Composable
fun SearchPostView(
    item: String,
    type: String,
    navController: NavHostController
) {
    val viewModel = PostViewModel()
    Get(viewModel, item, type)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            if (type == "share")
                SharePostListByTime(
                    posts = viewModel.searchedPosts.collectAsState(),
                    route = NAV_ROUTE.SHARE_DETAIL,
                    navHostController = navController)
            else{
                SeekPostList(
                    posts = viewModel.searchedPosts.collectAsState(),
                    route = NAV_ROUTE.SEEK_DETAIL,
                    navHostController = navController)
            }
        }
    }
}

@Composable
fun Get(viewModel: PostViewModel, item: String, type: String){
    viewModel.getPosts(
        item = item,
        category = null,
        reqType = "search",
        postType = type,
        currentIndex = 0,
        num = 20)
    { viewModel.searchedPosts.value = it}
}
