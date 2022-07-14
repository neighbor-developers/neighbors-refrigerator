package com.neighbor.neighborsrefrigerator.scenarios.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.navigation.NavAction
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SharePostDetail
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SeekPostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SharePostScreen
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Screen()
        }
    }
}
enum class NAV_ROUTE(val routeName:String, val description:String){
    MAIN_SHARE("MAIN_SHARE","나눔 리스트 화면"),
    MAIN_SEEK("MAIN_SEEK", "구함 리스트 화면"),
    SHARE_DETAIL("SHARE_DETAIL", "나눔 상세페이지"),
    SEEK_DETAIL("SEEK_DETAIL", "구함 상세페이지"),
    CHAT_LIST("CHAT_LIST", "채팅 리스트화면"),
    CHAT("CHAT", "채팅화면"),
    WRITE_SHARE("WRITE_SHARE", "나눔 글쓰기 페이지"),
    WRITE_SEEK("WRITE_SEEK", "구함 글쓰기 페이지")
}

class RouteAction(navHostController: NavHostController){
    //특정 페이지 이동
    val navTo: (NAV_ROUTE) -> Unit = { navRoute ->
        navHostController.navigate(navRoute.routeName)
    }

    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }
}

@Composable
fun Screen(startRoute: String= NAV_ROUTE.MAIN_SHARE.routeName){

    // 네비게이션 컨트롤러
    val navController = rememberNavController()

    // 네비게이션 라우트
    val routeAction = remember(navController) {RouteAction(navController)}

    // NavHost 로 네비게이션 결정
    NavHost(navController, startRoute){

        composable(NAV_ROUTE.MAIN_SHARE.routeName){
            MainScreen(routeAction = routeAction)
        }
        composable(route = NAV_ROUTE.SHARE_DETAIL.routeName){
            SharePostDetail(routeAction = routeAction, it.arguments?.getString("productID"))
        }
        composable(NAV_ROUTE.SEEK_DETAIL.routeName){
            MainScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.WRITE_SEEK.routeName) {
            MainScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.WRITE_SHARE.routeName){
            MainScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.CHAT.routeName){
            MainScreen(routeAction = routeAction)
        }
        composable(NAV_ROUTE.CHAT_LIST.routeName){
            MainScreen(routeAction = routeAction)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(routeAction: RouteAction) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        sheetBackgroundColor = Color.LightGray,
        sheetContentColor = Color.Black,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
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
                    IconButton(onClick = { routeAction.navTo(NAV_ROUTE.CHAT_LIST) }) {
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
                    DropdownMenuItem(onClick = { routeAction.navTo(NAV_ROUTE.WRITE_SHARE) }) {
                        Text("나눔")
                    }
                    DropdownMenuItem(onClick = { routeAction.navTo(NAV_ROUTE.WRITE_SEEK) }) {
                        Text("구함")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 40.dp
    ) {

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Row(){
                TextButton(onClick = { routeAction.navTo(NAV_ROUTE.MAIN_SHARE) }, modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)) {
                    Text(text = "나눔")
                }
                TextButton(onClick = { routeAction.navTo(NAV_ROUTE.MAIN_SEEK) }, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                    Text(text = "구함")
                }
            }
            SharePostScreen(
                sharePostViewModel = SharePostViewModel(),
                route = NAV_ROUTE.SHARE_DETAIL,
                routeAction = routeAction
            )
            //SeekPostScreen(sharePostViewModel = SharePostViewModel())
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

//@Preview
//@Composable
//fun DefaultPreview() {
//    MainScreen()
//}

//    var skipHalfExpanded by remember { mutableStateOf(false) }
//    val bottomSheetState = rememberBottomSheetScaffoldState()
//    val scope = rememberCoroutineScope()

//    Scaffold(
//        scaffoldState = scaffoldState,
//        topBar = {
//            TopAppBar(
//                title = {
//                },
//                actions = {
//                    IconButton(onClick = { /* 채팅 페이지 이동*/ }) {
//                        Icon(Icons.Filled.Send, contentDescription = "")
//                    }
//                    IconButton(onClick = { /* 마이페이지 드로어 */ }) {
//                        Icon(Icons.Filled.Menu, contentDescription = "")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                backgroundColor = MaterialTheme.colors.background
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { isDropDownMenuExpanded = true },
//                contentColor = Color.Black,
//                backgroundColor = Color.Yellow
//            ) {
//                Icon(Icons.Filled.Edit, contentDescription = "a")
//                DropdownMenu(
//                    modifier = Modifier.wrapContentSize(),
//                    expanded = isDropDownMenuExpanded,
//                    onDismissRequest = { isDropDownMenuExpanded = false },
//                    offset = DpOffset((10).dp, 0.dp)
//                ) {
//                    DropdownMenuItem(onClick = { /*나눔 글쓰기 페이지 이동*/ }) {
//                        Text("나눔")
//                    }
//                    DropdownMenuItem(onClick = { /*구함 글쓰기 페이지 이동*/ }) {
//                        Text("구함")
//                    }
//                }
//            }
//        },
//        floatingActionButtonPosition = FabPosition.End,
//        isFloatingActionButtonDocked = false
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(it)
//                .fillMaxSize()
//        ) {
//            Row() {
//                TextButton(
//                    onClick = { /*나눔페이지*/ },
//                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
//                ) {
//                    Text(text = "나눔")
//                }
//                TextButton(
//                    onClick = { /*구함페이지*/ },
//                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
//                ) {
//                    Text(text = "구함")
//                }
//            }
//            SharePostScreen(
//                sharePostViewModel = SharePostViewModel()
//            )
//        }
//        Column(
//            modifier = Modifier.fillMaxSize().padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Row(
//                Modifier.toggleable(
//                    value = skipHalfExpanded,
//                    role = Role.Checkbox,
//                    onValueChange = { checked -> skipHalfExpanded = checked }
//                )
//            ) {
//                Checkbox(checked = skipHalfExpanded, onCheckedChange = null)
//                Spacer(Modifier.width(16.dp))
//                Text("Skip Half Expanded State")
//            }
//            Spacer(Modifier.height(20.dp))
//            Button(onClick = { scope.launch { bottomSheetState.show() } }) {
//                Text("Click to show sheet")
//            }
//        }

//    ModalBottomSheetLayout(
//        sheetState = bottomSheetState,
//        sheetContent = {
//            Column(Modifier.padding(20.dp)) {
//                Text(text = "어쩌구~")
//                Text(text = "어쩌구~")
//                Text(text = "어쩌구~")
//                Text(text = "어쩌구~")
//                Text(text = "어쩌구~")
//            }
//        }
//    ) {
//    }
//}
