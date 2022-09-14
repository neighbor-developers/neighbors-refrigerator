package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.intro.DotsIndicator
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import kotlinx.coroutines.launch

@Composable
fun SharePostScreen(
    postViewModel: PostViewModel,
    route: NAV_ROUTE,
    navController: NavHostController
) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(0) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .height(900.dp)
            .background(colorResource(id = R.color.backgroundGray))
    ) {
        SharePostListByDistance(posts = postViewModel.sharePostsByDistance.collectAsState(), route, navController)
        Spacer(modifier = Modifier
            .height(15.dp)
            .background(color = colorResource(id = R.color.green)))
        Tab(postViewModel = postViewModel, route, navController)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tab(postViewModel: PostViewModel, route: NAV_ROUTE, navHostController: NavHostController)
{
    val categoryList = mapOf(0 to "전체", 100 to "채소", 200 to "과일", 300 to "정육", 400 to "수산", 500 to "냉동식품", 600 to "간편식품")
    val categoryIconList = mapOf(0 to R.drawable.category_all, 100 to R.drawable.category_100, 200 to R.drawable.category_200, 300 to R.drawable.category_300, 400 to R.drawable.category_400, 500 to R.drawable.category_500, 600 to R.drawable.category_600)

    val pagerState = rememberPagerState(pageCount = 7)
    val coroutineScope = rememberCoroutineScope()


    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "", tint = colorResource(
            id = R.color.green
        ), modifier = Modifier
            .size(18.dp)
            .padding(start = 10.dp))
        Text(
            text = "우리 동네 나눔",
            modifier = Modifier.padding(start = 5.dp, bottom = 10.dp, top = 10.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
    DrawLine()
    TabRow(
        modifier = Modifier.height(60.dp),
        selectedTabIndex = pagerState.currentPage,
        indicator = {
            tabPositions -> 
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        backgroundColor = Color.White,
        contentColor = colorResource(id = R.color.categoryGreen)
    ){
        categoryList.keys.forEachIndexed{ index, title ->
            Tab(
                onClick = {
                    coroutineScope.launch { 
                        pagerState.scrollToPage(index)
                    }
                },
                selected = pagerState.currentPage == index,
                content = {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                            Icon(painter = painterResource(id = categoryIconList[title]!!), contentDescription = categoryList[title], modifier = Modifier.size(if (title == 200) 31.dp else if (title == 0) 30.dp else 35.dp),
                                tint = colorResource(id = R.color.categoryGreen))
                            Text(text = categoryList[title]!!, fontSize = 10.sp, color =  colorResource(id = R.color.categoryGreen), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        }
//                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
//
//                            Icon(painter = painterResource(id = categoryIconList[title]!!), contentDescription = categoryList[title], modifier = Modifier.size(if (title == 200) 31.dp else if (title == 0) 30.dp else 35.dp),
//                                tint = if(pagerState.currentPage == index) Color.White else colorResource(id = R.color.green))
//                            Text(text = categoryList[title]!!, fontSize = 10.sp, color = if(pagerState.currentPage == index) Color.White else colorResource(id = R.color.green), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
//                        }
                    }
                },
                selectedContentColor = Color.Transparent,
                unselectedContentColor = Color.Transparent
            )
        }
    }
    
    HorizontalPager(state = pagerState) { page ->
        SharePostListByTime(posts = when(page) {
            0 -> postViewModel.sharePostsByTime.collectAsLazyPagingItems()
            1 -> postViewModel.sharePostsForCategory100.collectAsLazyPagingItems()
            2 -> postViewModel.sharePostsForCategory200.collectAsLazyPagingItems()
            3 -> postViewModel.sharePostsForCategory300.collectAsLazyPagingItems()
            4 -> postViewModel.sharePostsForCategory400.collectAsLazyPagingItems()
            5 -> postViewModel.sharePostsForCategory500.collectAsLazyPagingItems()
            6 -> postViewModel.sharePostsForCategory600.collectAsLazyPagingItems()
            else -> postViewModel.sharePostsByTime.collectAsLazyPagingItems()}, route = route, navHostController = navHostController)

    }

}

@Composable
fun SharePostListByTime(posts: LazyPagingItems<PostData>, route: NAV_ROUTE, navHostController: NavHostController){
    LazyColumn(
        userScrollEnabled = true,
        modifier = Modifier
            .background(colorResource(id = R.color.backgroundGray))
    ) {
        items(posts.itemCount) { count ->
            Spacer(modifier = Modifier
                .height(15.dp)
                .background(Color.Transparent))
            ItemCardByTime(post= posts[count]!!, route = route, navHostController = navHostController)

        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SharePostListByDistance(posts: State<ArrayList<PostData>?>, route: NAV_ROUTE, navHostController: NavHostController){

    val userNickname by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("nickname")!!)
    }
    val pagerState = rememberPagerState(pageCount = 6)
    if (!posts.value.isNullOrEmpty()){

        DrawLine()
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "", tint = colorResource(
                id = R.color.green
            ), modifier = Modifier
                .size(18.dp)
                .padding(start = 10.dp))
            Text(
                text = "${userNickname}님에게 가까운 나눔",
                modifier = Modifier.padding(start = 5.dp, bottom = 10.dp, top = 10.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        DrawLine()
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), contentAlignment = Alignment.BottomCenter) {
            HorizontalPager(state = pagerState) { page ->
                ItemCardByTime(
                    post = posts.value!![page],
                    route = route,
                    navHostController = navHostController
                )
            }
            DotsIndicator(
                totalDots = 6,
                selectedIndex = pagerState.currentPage,
                selectedColor = colorResource(id = R.color.green),
                unSelectedColor = Color.LightGray,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

    }

}
