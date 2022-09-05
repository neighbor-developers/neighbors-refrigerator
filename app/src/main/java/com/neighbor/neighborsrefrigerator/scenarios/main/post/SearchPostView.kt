package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.network.MyPagingSource
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.ReqPostData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchPostView(
    item: String,
    type: String,
    navController: NavHostController,
    postViewModel: PostViewModel = viewModel()
) {
    val timeStamp = remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyy-MM-dd HH:MM:ss",
                Locale.KOREA
            ).format(Date(System.currentTimeMillis()))
        )
    }
    val search = remember {
        mutableStateOf<Flow<PagingData<PostData>>?>(null)
    }
    val job = CoroutineScope(Dispatchers.Main)
    LaunchedEffect(Unit){
        search.value = Pager(
            PagingConfig(pageSize = 20)
        )
        { MyPagingSource(ReqPostData(2, postType = when (type) {
            "share" -> 1
            "seek" -> 2
            else -> 1
        },null, item, currentTime = timeStamp.value, 37.3402, 126.7335))
        }.flow.cachedIn(job)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                        job.cancel()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            search.value?.let {
                if (type == "share")
                    SharePostListByTime(
                        posts = it.collectAsLazyPagingItems(),
                        route = NAV_ROUTE.SHARE_DETAIL,
                        navHostController = navController
                    )
                else {
                    SeekPostList(
                        posts = it.collectAsLazyPagingItems(),
                        route = NAV_ROUTE.SEEK_DETAIL,
                        navHostController = navController,
                        postViewModel
                    )
                }
            }
        }
    }
}
