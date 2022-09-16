package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
            .background(Color.White)
    ) {
        SharePostListByDistance(posts = postViewModel.sharePostsByDistance.collectAsState(), route, navController)
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


    Row(
        modifier = Modifier.padding(start = 15.dp, bottom = 10.dp, top = 18.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "",
            tint = colorResource(id = R.color.green),
            modifier = Modifier.size(18.dp))
        Text(
            text = "우리 동네 나눔",
            modifier = Modifier.padding(start = 5.dp),
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

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalPager(state = pagerState) { page ->
        val category = if (page == 0) null else page.toString() + "00"
        SharePostListByTime(postItems = postViewModel.sharePostsByTime.collectAsLazyPagingItems(), route = route, navHostController = navHostController, category = category)
    }

}

@Composable
fun SharePostListByTime(postItems: LazyPagingItems<PostData>, route: NAV_ROUTE, navHostController: NavHostController, category : String?){
    val posts =
        if (!category.isNullOrEmpty()) {
            postItems.itemSnapshotList.items.filter {
                it.categoryId == category.toString()
            }
        }else{
            postItems.itemSnapshotList.items
        }

    LazyColumn(
        userScrollEnabled = true,
        modifier = Modifier
            .background(Color.White)
    ) {
        items(posts){ post ->
            ItemCardByTime(post= post, route = route, navHostController = navHostController, type = 1)

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SharePostListByDistance(posts: State<List<PostData>?>, route: NAV_ROUTE, navHostController: NavHostController){

    val userNickname by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("nickname")!!)
    }
    val pagerState = rememberPagerState(pageCount = 6)
    if (!posts.value.isNullOrEmpty()){

        Column {
            DrawLine2()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White).padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                    tint = colorResource(
                        id = R.color.green
                    ),
                    modifier = Modifier
                        .size(18.dp)
                )
                Text(
                    text = "${userNickname}님에게 가까운 나눔",
                    modifier = Modifier.padding(start = 5.dp, bottom = 10.dp, top = 10.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            DrawLine()
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val (item, dot) = createRefs()
                HorizontalPager(state = pagerState, modifier = Modifier
                    .height(115.dp)
                    .constrainAs(item) {
                        top.linkTo(parent.top)
                    }) { page ->
                    ItemCardByTime(
                        post = posts.value!![page],
                        route = route,
                        navHostController = navHostController,
                        type = 2
                    )
                }
                DotsIndicator(
                    totalDots = 6,
                    selectedIndex = pagerState.currentPage,
                    selectedColor = colorResource(id = R.color.green),
                    unSelectedColor = Color.LightGray,
                    modifier = Modifier.constrainAs(dot) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }

    }

}
@Composable
fun DrawLine2(){
    val color = colorResource(id = R.color.completeGray)
    Canvas(modifier = Modifier.fillMaxWidth()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = 2.5f
        )
    }
}

