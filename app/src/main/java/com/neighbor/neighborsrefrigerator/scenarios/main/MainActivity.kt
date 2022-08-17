package com.neighbor.neighborsrefrigerator.scenarios.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.scenarios.intro.RegisterInfo
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ChatListScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ChatScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ReviewScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.drawer.Drawer
import com.neighbor.neighborsrefrigerator.scenarios.main.post.SearchPostView
import com.neighbor.neighborsrefrigerator.scenarios.main.post.SeekPostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.SharePostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.detail.SeekPostDetailScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.detail.SharePostDetailScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.register.SharePostRegisterScreen
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.hasId.collect{
                // 아이디 존재할경우 메인, 존재하지 않을경우 등록 페이지
                val route = if(it){
                    NAV_ROUTE.MAIN.routeName
                }else{
                    NAV_ROUTE.REGISTER_INFO.routeName
                }
                setContent {
                    Screen(startRoute = route)
                }
            }
        }
    }
}



enum class NAV_ROUTE(val routeName:String, val description:String){
    MAIN("MAIN","나눔/구함 리스트 화면"),
    SHARE_DETAIL("SHARE_DETAIL", "나눔 상세페이지"),
    SEEK_DETAIL("SEEK_DETAIL", "구함 상세페이지"),
    SHARE_REGISTER("SHARE_REGISTER", "나눔 등록페이지"),
    SEEK_REGISTER("SEEK_REGISTER", "구함 등록페이지"),
    CHAT_LIST("CHAT_LIST", "채팅 리스트화면"),
    CHAT("CHAT", "채팅화면"),
    SETTING("SETTING", "설정화면"),
    TRADE_HISTORY("TRADE_HISTORY", "거래 내역"),
    REVIEW("REVIEW", "리뷰작성"),
    SEARCH_POST("SEARCH_POST", "검색된 리스트화면"),
    REGISTER_INFO("REGISTER_INFO", "계정등록")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen(startRoute: String){

    // 네비게이션 컨트롤러
    val navController = rememberNavController()

    // NavHost 로 네비게이션 결정
    NavHost(navController, startRoute){

        composable(NAV_ROUTE.MAIN.routeName){
            MainScreen(navController)
        }

        composable(NAV_ROUTE.REGISTER_INFO.routeName){
            RegisterInfo(navController)
        }

        composable(NAV_ROUTE.SHARE_DETAIL.routeName){
            val post = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<PostData>("post")
            }
            SharePostDetailScreen(navController, post!!)
        }
        composable(NAV_ROUTE.SEEK_DETAIL.routeName){
            val post = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<PostData>("post")
            }
            SeekPostDetailScreen(navController, post!!)
        }
        composable(NAV_ROUTE.SHARE_REGISTER.routeName){
            SharePostRegisterScreen(navController)
        }
        composable(NAV_ROUTE.SEEK_REGISTER.routeName){

        }
        composable("${NAV_ROUTE.CHAT.routeName}/{chatID}", arguments = listOf(navArgument("chatId"){type = NavType.StringType})) {
            ChatScreen(navController, it.arguments?.getString("chatId")?:"")
        }
        composable(NAV_ROUTE.CHAT_LIST.routeName){
            ChatListScreen(navController)
        }
        composable(NAV_ROUTE.SETTING.routeName){
            Setting()
        }
        composable(NAV_ROUTE.TRADE_HISTORY.routeName){

        }
        composable(NAV_ROUTE.REVIEW.routeName){
            val post = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<PostData>("post")
            }
            ReviewScreen(post!!, navController)
        }
        composable("${NAV_ROUTE.SEARCH_POST.routeName}/{item}/{type}", arguments = listOf(navArgument("item"){type = NavType.StringType}, navArgument("type"){type = NavType.StringType})){
            SearchPostView(item = it.arguments?.getString("item")?:"", type = it.arguments?.getString("type")?:"share", navController = navController,)
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {

    val scaffoldState = rememberScaffoldState()
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    val types = remember { mutableStateOf("share") }

    val scope = rememberCoroutineScope()

    Scaffold(
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
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }}) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
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
                    DropdownMenuItem(onClick = { navController.navigate(NAV_ROUTE.SHARE_REGISTER.routeName) }) {
                        Text("나눔")
                    }
                    DropdownMenuItem(onClick = { navController.navigate(NAV_ROUTE.SEEK_REGISTER.routeName) }) {
                        Text("구함")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        drawerContent = {
            Drawer(navController)
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen
    ) {

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Row(){
                Button(
                    onClick = {
                        types.value = "share" },
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.White, disabledBackgroundColor = Color.Yellow, disabledContentColor = Color.Black),
                    enabled = when(types.value){
                        "share" -> false
                        "seek" -> true
                        else -> true
                    }
                ) {
                    Text(text = "나눔")
                }
                Button(
                    onClick = { types.value = "seek" },
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.White, disabledBackgroundColor = Color.Yellow, disabledContentColor = Color.Black),
                    enabled = when(types.value){
                        "share" -> true
                        "seek" -> false
                        else -> true
                    }
                ) {
                    Text(text = "구함")
                }
            }
            when(types.value){
                "share" -> SharePostScreen(
                    postViewModel = PostViewModel(),
                    route = NAV_ROUTE.SHARE_DETAIL,
                    navController = navController
                )
                "seek" -> SeekPostScreen(
                    postViewModel = PostViewModel(),
                    route = NAV_ROUTE.SEEK_DETAIL,
                    navHostController = navController
                )
            }

        }
    }
}
