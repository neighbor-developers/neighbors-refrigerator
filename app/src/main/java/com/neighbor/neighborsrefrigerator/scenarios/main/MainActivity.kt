package com.neighbor.neighborsrefrigerator.scenarios.main

/*import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ChatListScreen*/
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.scenarios.intro.StartActivity
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ChatListScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ChatScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.chat.ReviewScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.drawer.Drawer
import com.neighbor.neighborsrefrigerator.scenarios.main.post.SearchPostView
import com.neighbor.neighborsrefrigerator.scenarios.main.post.SeekPostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.SharePostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.detail.SharePostDetailScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.register.SeekPostRegisterScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.post.register.SharePostRegisterScreen
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val dbAccessModule = DBAccessModule()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleLogin()

        setContent {
            Screen(mainViewModel = viewModel, startRoute = NAV_ROUTE.MAIN.routeName)
        }

        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    MainViewModel.MainEvent.LogOut -> logOut()
                    MainViewModel.MainEvent.DelAuth -> delAuth()
                }
            }

        }
    }


    // 로그인 객체 생성
    private fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // 빨간줄이지만 토큰 문제라 실행 가능
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun logOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            Log.d("logOut", "로그아웃 완료")
        }

        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)

    }

    private fun delAuth() {
        lifecycleScope.launch {
            val id = UserSharedPreference(App.context()).getUserPrefs("id")
            var result = false
            lifecycleScope.async {
                result = dbAccessModule.deleteUser(id!!.toInt())
            }.await()
            if (result) {
                auth.signOut()
                googleSignInClient.signOut().addOnCompleteListener {
                    Log.d("logOut", "로그아웃 완료")
                }
                viewModel.delChatData()
                toStartActivity()
            }
        }

    }
    private fun toStartActivity(){
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

}



enum class NAV_ROUTE(val routeName:String, val description:String){
    MAIN("MAIN","나눔/구함 리스트 화면"),
    SHARE_DETAIL("SHARE_DETAIL", "나눔 상세페이지"),
    SHARE_REGISTER("SHARE_REGISTER", "나눔 등록페이지"),
    SEEK_REGISTER("SEEK_REGISTER", "구함 등록페이지"),
    CHAT_LIST("CHAT_LIST", "채팅 리스트화면"),
    CHAT("CHAT", "채팅화면"),
    SETTING("SETTING", "설정화면"),
    TRADE_HISTORY("TRADE_HISTORY", "거래 내역"),
    REVIEW("REVIEW", "리뷰작성"),
    SEARCH_POST("SEARCH_POST", "검색된 리스트화면"),
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen(mainViewModel: MainViewModel, startRoute: String){

    // 네비게이션 컨트롤러
    val navController = rememberNavController()
    var userId = UserSharedPreference(App.context()).getUserPrefs("id")

    // NavHost 로 네비게이션 결정
    NavHost(navController, startRoute){

        composable(NAV_ROUTE.MAIN.routeName){
            MainScreen(viewModel = mainViewModel, navController)
        }
        composable(NAV_ROUTE.SHARE_DETAIL.routeName){
            val post = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<PostData>("post")
            }
            SharePostDetailScreen(navHostController = navController, post = post!!)
        }

        composable(NAV_ROUTE.SHARE_REGISTER.routeName){
            SharePostRegisterScreen(navController)
        }
        composable(NAV_ROUTE.SEEK_REGISTER.routeName){
            SeekPostRegisterScreen(navController)
        }
        composable("${NAV_ROUTE.CHAT.routeName}/{chatId}/{postId}", arguments = listOf(navArgument("chatId"){type = NavType.StringType},navArgument("postId"){type = NavType.IntType})) {
            ChatScreen(navController = navController, chatId = it.arguments?.getString("chatId")?:"", postId = it.arguments?.getInt("postId") ?:0)
        }
        composable(NAV_ROUTE.CHAT_LIST.routeName){
            ChatListScreen(navController)
        }
        composable(NAV_ROUTE.SETTING.routeName){
            Setting(navController = navController,mainViewModel = mainViewModel)
        }
        composable("${NAV_ROUTE.TRADE_HISTORY.routeName}/{userId}", arguments = listOf(navArgument("userId"){type = NavType.IntType})){
            ShowUserProfile(
                navController = navController,
                userId = it.arguments?.getInt("userId") ?:0)
        }
        composable(NAV_ROUTE.REVIEW.routeName){
            val post = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<PostData>("post")
            }
            ReviewScreen(post!!, navController)
        }
        composable("${NAV_ROUTE.SEARCH_POST.routeName}/{item}/{type}", arguments = listOf(navArgument("item"){type = NavType.StringType}, navArgument("type"){type = NavType.StringType})){
            SearchPostView(
                item = it.arguments?.getString("item") ?: "",
                type = it.arguments?.getString("type") ?: "share",
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController) {

    val scaffoldState = rememberScaffoldState()
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    val types = remember { mutableStateOf("share") }

    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement =Arrangement.Bottom) {
                        Image(painter = painterResource(id = R.drawable.level1), contentDescription = "", modifier = Modifier.size(30.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(NAV_ROUTE.CHAT_LIST.routeName) }) {
                        Icon(painter = painterResource(id = R.drawable.icon_chat2), contentDescription = "", modifier = Modifier.size(28.dp), tint = colorResource(
                            id = R.color.green
                        ))
                    }

                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }}) {
                        Icon(painterResource(id = R.drawable.icon_menu), contentDescription = "", modifier = Modifier.size(35.dp), tint = colorResource(
                            id = R.color.green))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isDropDownMenuExpanded = true }, contentColor = Color.Black, backgroundColor = Color.White) {
                Icon(painter = painterResource(id = R.drawable.icon_add), contentDescription = "add", modifier = Modifier.size(30.dp), tint = colorResource(
                    id = R.color.green
                ))
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false }
                ) {
                    DropdownMenuItem(onClick = { navController.navigate(NAV_ROUTE.SHARE_REGISTER.routeName) }) {
                        Text("나눔 글쓰기")
                    }
                    DropdownMenuItem(onClick = { navController.navigate(NAV_ROUTE.SEEK_REGISTER.routeName) }) {
                        Text("구함 글쓰기")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        drawerContent = {
            Drawer(viewModel = viewModel, navController)
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {

            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 8.dp)){
                Button(
                    onClick = { types.value = "share" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = colorResource(id = R.color.green), disabledBackgroundColor = colorResource(id = R.color.green), disabledContentColor = Color.White),
                    enabled = when(types.value){
                        "share" -> false
                        "seek" -> true
                        else -> true
                    },
                    elevation = ButtonDefaults.elevation(0.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "나눔",
                            color = if(types.value == "share") Color.White else Color.Black,
                            fontWeight = if (types.value == "share") FontWeight.Bold else FontWeight.Normal
                        )
//
                    }

                }
                Button(
                    onClick = { types.value = "seek" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = colorResource(id = R.color.green), disabledBackgroundColor = colorResource(id = R.color.green), disabledContentColor = Color.White),
                    enabled = when(types.value){
                        "share" -> true
                        "seek" -> false
                        else -> true
                    },
                    elevation = ButtonDefaults.elevation(0.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "구함",
                            color = if(types.value == "seek") Color.White else Color.Black,
                            fontWeight = if (types.value == "seek") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
                Surface(modifier = Modifier
                    .weight(4f)
                    .padding(start = 6.dp)) {
                    SearchBox(type = types.value, navController = navController)
                }
            }
            val postViewModel by remember {
                mutableStateOf(PostViewModel())
            }
            when(types.value){
                "share" -> SharePostScreen(
                    postViewModel,
                    route = NAV_ROUTE.SHARE_DETAIL,
                    navController = navController
                )
                "seek" -> SeekPostScreen(
                    postViewModel,
                    navHostController = navController
                )
            }

        }
    }
}

@Composable
fun SearchBox(type: String, navController: NavHostController) {
    var item by remember { mutableStateOf("") }
    Box(
        contentAlignment = Alignment.CenterEnd
    ) {
        val cornerSize = CornerSize(20.dp)
        TextField(
            value = item,
            onValueChange = { item = it },
            modifier = Modifier
                .height(45.dp),
            shape = MaterialTheme.shapes.large.copy(cornerSize),
            placeholder = { Text(text = "검색하기", style = TextStyle(fontSize = 11.5.sp, textDecoration = TextDecoration.None), modifier = Modifier.padding(bottom = 0.dp)) },
            maxLines = 1,
            trailingIcon = {
                IconButton(
                    onClick = {
                        if(item.isNotEmpty() && item != " ") {
                            navController.navigate("${NAV_ROUTE.SEARCH_POST.routeName}/$item/$type")
                        }
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_search),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = colorResource(id = R.color.green)
                    )
                }
            },
            visualTransformation = VisualTransformation.None
            ,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.DarkGray,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.DarkGray,
                backgroundColor = colorResource(id = R.color.backgroundGray),
            ),
            textStyle = TextStyle(fontSize = 13.sp, textDecoration = TextDecoration.None),

        )
    }
}
