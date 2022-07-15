package com.neighbor.neighborsrefrigerator.scenarios.main

import android.graphics.ComposePathEffect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SeekPostDetail
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SharePostDetail
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SeekPostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SharePostScreen
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Screen()
        }
    }
}
enum class NAV_ROUTE(val routeName:String, val description:String){
    MAIN("MAIN","나눔/구함 리스트 화면"),
    START("MAIN_2", "시작(나눔 리스트 화면)"),
    SHARE_DETAIL("SHARE_DETAIL", "나눔 상세페이지"),
    SEEK_DETAIL("SEEK_DETAIL", "구함 상세페이지"),
    CHAT_LIST("CHAT_LIST", "채팅 리스트화면"),
    CHAT("CHAT", "채팅화면"),
    WRITE_SHARE("WRITE_SHARE", "나눔 글쓰기 페이지"),
    WRITE_SEEK("WRITE_SEEK", "구함 글쓰기 페이지")
}

@Composable
fun Screen(startRoute: String= NAV_ROUTE.START.routeName){

    // 네비게이션 컨트롤러
    val navController = rememberNavController()

    // NavHost 로 네비게이션 결정
    NavHost(navController, startRoute){

        composable(NAV_ROUTE.START.routeName){
            MainScreen(navController, 1)
        }
        composable("${NAV_ROUTE.MAIN.routeName}/{type}", arguments = listOf(navArgument("type"){
            type = NavType.IntType
            defaultValue = 1}))
        {
            MainScreen(navController, it.arguments?.getInt("type")?:1)
        }
        composable("${NAV_ROUTE.SHARE_DETAIL.routeName}/{productID}", arguments = listOf(navArgument("productID"){type = NavType.StringType})){
            SharePostDetail(navController, it.arguments?.getString("productID"))
        }
        composable("${NAV_ROUTE.SEEK_DETAIL.routeName}/{postID}", arguments = listOf(navArgument("postID"){type = NavType.StringType})){
            SeekPostDetail(navController, it.arguments?.getString("postID"))
        }
        composable(NAV_ROUTE.WRITE_SEEK.routeName) {

        }
        composable(NAV_ROUTE.WRITE_SHARE.routeName){

        }
        composable(NAV_ROUTE.CHAT.routeName){

        }
        composable(NAV_ROUTE.CHAT_LIST.routeName){

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController, type: Int?) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        sheetBackgroundColor = Color.LightGray,
        sheetContentColor = Color.Black,
        sheetShape = MaterialTheme.shapes.small.copy(topStart = CornerSize(40.dp), topEnd = CornerSize(40.dp)),
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Category")
            }
            /*카테고리 내용*/

             categoryView(categoryList = listOf("a", "s", "d", "a", "s", "d", "s", "s"))
        },
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                },
                actions = {
                    IconButton(onClick = { navController.navigate(NAV_ROUTE.CHAT_LIST.routeName) }) {
                        Icon(Icons.Filled.Send, contentDescription = "")
                    }

                },
                navigationIcon = {
                    IconButton(onClick = { /* 마이페이지 드로어 */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isDropDownMenuExpanded = true }, contentColor = Color.Black, backgroundColor = Color.Yellow) {
                Icon(Icons.Filled.Edit, contentDescription = "a")
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false },
                    offset = DpOffset((10).dp, 0.dp)
                ) {
                    DropdownMenuItem(onClick = { navController.navigate(NAV_ROUTE.WRITE_SHARE.routeName) }) {
                        Text("나눔")
                    }
                    DropdownMenuItem(onClick = { navController.navigate(NAV_ROUTE.WRITE_SEEK.routeName) }) {
                        Text("구함")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 35.dp
    ) {

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Row(){
                Button(
                    onClick = { navController.navigate("${NAV_ROUTE.MAIN.routeName}/${1}") },
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow, contentColor = Color.Black, disabledBackgroundColor = Color.LightGray, disabledContentColor = Color.White),
                    enabled = when(type){
                        1 -> false
                        else -> true
                    }
                ) {
                    Text(text = "나눔")
                }
                Button(
                    onClick = { navController.navigate("${NAV_ROUTE.MAIN.routeName}/${2}") },
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow, contentColor = Color.Black, disabledBackgroundColor = Color.LightGray, disabledContentColor = Color.White),
                    enabled = when(type){
                        1 -> true
                        else -> false
                    }
                ) {
                    Text(text = "구함")
                }
            }
            when(type){
                1 -> SharePostScreen(
                    sharePostViewModel = SharePostViewModel(),
                    route = NAV_ROUTE.SHARE_DETAIL,
                    navHostController = navController
                )
                2 -> SeekPostScreen(
                    sharePostViewModel = SharePostViewModel(),
                    route = NAV_ROUTE.SEEK_DETAIL,
                    navHostController = navController
                )
            }

        }
    }
}

 @Composable
 fun categoryView(categoryList: List<String>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(30.dp)
    ) {
        items(categoryList){ item ->
            CategoryItemView(item)
        }

    }
 }

 @Composable
 fun CategoryItemView(item: String){
    Card() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.Favorite, contentDescription = item, modifier = Modifier.height(40.dp))
            Text(text = "야채")
        }
    }
 }