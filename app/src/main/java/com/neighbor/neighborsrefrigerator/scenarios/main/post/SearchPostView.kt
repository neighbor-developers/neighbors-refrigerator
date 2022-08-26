package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel

@Composable
fun SearchPostView(
    item: String,
    type: String,
    navController: NavHostController,
    postViewModel: PostViewModel = viewModel()
) {
    postViewModel.getPosts(
        item = item,
        category = null,
        reqType = "search",
        postType = type,
        varType = 3
    )

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
                    posts = postViewModel.searchedPosts.collectAsLazyPagingItems(),
                    route = NAV_ROUTE.SHARE_DETAIL,
                    navHostController = navController)
            else{
                SeekPostList(
                    posts = postViewModel.searchedPosts.collectAsLazyPagingItems(),
                    route = NAV_ROUTE.SEEK_DETAIL,
                    navHostController = navController)
            }
        }
    }
}
