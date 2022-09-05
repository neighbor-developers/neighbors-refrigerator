package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel

@Composable
fun SharePostScreen(
    postViewModel: PostViewModel,
    route: NAV_ROUTE,
    navController: NavHostController
) {
    val state = rememberScrollState()
    val category = postViewModel.category.collectAsState()
    LaunchedEffect(Unit) { state.animateScrollTo(0) }

    Column(
        modifier = Modifier
            .verticalScroll(state)
            .height(1000.dp)
    ) {
        SharePostListByDistance(posts = postViewModel.sharePostsByDistance.collectAsState(), route, navController)

        CategoryView(postViewModel)
        SharePostListByTime(posts = when(category.value){
            0 -> postViewModel.sharePostsByTime.collectAsLazyPagingItems()
            1 -> postViewModel.sharePostsForCategory100.collectAsLazyPagingItems()
            2 -> postViewModel.sharePostsForCategory200.collectAsLazyPagingItems()
            3 -> postViewModel.sharePostsForCategory300.collectAsLazyPagingItems()
            4 -> postViewModel.sharePostsForCategory400.collectAsLazyPagingItems()
            5 -> postViewModel.sharePostsForCategory500.collectAsLazyPagingItems()
            6 -> postViewModel.sharePostsForCategory600.collectAsLazyPagingItems()
            else -> postViewModel.sharePostsByTime.collectAsLazyPagingItems()
                                                        },
        route = route, navController)
    }
}



@Composable
fun SharePostListByTime(posts : LazyPagingItems<PostData>,route: NAV_ROUTE, navHostController: NavHostController)
{
    val scrollState = rememberLazyGridState()

    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
        userScrollEnabled = true,
    ) {
        items(posts.itemCount) { count ->
            ItemCardByTime(posts[count]!!, route, navHostController)

        }

    }
}


@Composable
fun CategoryView(postViewModel: PostViewModel){
    val categoryList = mapOf(0 to "전체", 100 to "채소", 200 to "과일", 300 to "정육", 400 to "수산", 500 to "냉동식품", 600 to "간편식품")
    val categoryIconList = mapOf(0 to R.drawable.category_all, 100 to R.drawable.category_100, 200 to R.drawable.category_200, 300 to R.drawable.category_300, 400 to R.drawable.category_400, 500 to R.drawable.category_500, 600 to R.drawable.category_600)

    var selectedCategory by remember {
        mutableStateOf("전체")
    }
    Row(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        categoryList.forEach { category ->
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {
                    selectedCategory = category.value
                    postViewModel.category.value = category.key

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = if(selectedCategory == category.value) colorResource(id = R.color.green) else Color.White),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.elevation(0.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                    Icon(painterResource(id = categoryIconList[category.key]!!), contentDescription = category.value, modifier = Modifier.size(if (category.key == 200) 31.dp else if (category.key == null) 30.dp else 35.dp),
                        tint = if(selectedCategory == category.value) Color.White else colorResource(id = R.color.green))
                    Text(text = category.value, fontSize = 10.sp, color = if(selectedCategory == category.value) Color.White else colorResource(id = R.color.green), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun SharePostListByDistance(posts: State<ArrayList<PostData>?>, route: NAV_ROUTE, navHostController: NavHostController){
    val userNickname by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("nickname")!!)
    }
    if (!posts.value.isNullOrEmpty()){
        Text(
            text = "# ${userNickname}님 위치에서 가까운 나눔",
            modifier = Modifier.padding(start = 30.dp, end = 15.dp, top = 30.dp, bottom = 10.dp),
            fontSize = 15.sp
        )
        LazyRow (modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp))
        {
            posts.value?.let {
                items(it){ item ->
                    ItemCardByDistance(post = item, route, navHostController)
                }
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 20.dp)
        ) {
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = 0f, y = canvasHeight),
                end = Offset(x = this.size.width, y = canvasHeight),
                color = Color.DarkGray,
                strokeWidth = 1F
            )
        }
    }
}
