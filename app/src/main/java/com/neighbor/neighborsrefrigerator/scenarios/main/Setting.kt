package com.neighbor.neighborsrefrigerator.scenarios.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.datatransport.BuildConfig
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.LoginViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel

//@Composable
//private fun animateAlignmentAsState(
//    targetBiasValue: Float
//): State<BiasAlignment> {
//    val bias by animateFloatAsState(targetBiasValue)
//    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
//}

@Composable
fun Setting(navController: NavHostController, loginViewModel: LoginViewModel = viewModel(), mainViewModel: MainViewModel) {
    val version = "버전 정보 : ${BuildConfig.VERSION_NAME}"
    val scaffoldState = rememberScaffoldState()

    val chatCheckedState = remember {
        mutableStateOf(UserSharedPreference(App.context()).getNoticePref("chat"))
    }
    val reviewCheckedState = remember {
        mutableStateOf(UserSharedPreference(App.context()).getNoticePref("review"))
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "설정", textAlign = TextAlign.Center, fontSize = 17.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painterResource(id = R.drawable.icon_back), contentDescription = "뒤로가기", modifier = Modifier.size(35.dp), tint = colorResource(
                            id = R.color.green)
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.backgroundGray)
            ) {
                Text(text = "알림 설정", Modifier.padding(10.dp), color = Color.DarkGray, fontSize = 13.sp)
            }
            ConstraintLayout(modifier = Modifier
                .padding(top = 15.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
                .fillMaxWidth()) {
                val (button, switch) = createRefs()
                Text(
                    text = "채팅 알림",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.constrainAs(button){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
                Switch(
                    checked = chatCheckedState.value,
                    onCheckedChange = {
                        chatCheckedState.value = it
                        UserSharedPreference(App.context()).setNoticePref("chat", chatCheckedState.value)
                    },
                    modifier = Modifier.constrainAs(switch){
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }
            ConstraintLayout(modifier = Modifier
                .padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 15.dp)
                .fillMaxWidth()) {
                val (button, switch) = createRefs()
                Text(
                    text = "후기 알림",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.constrainAs(button){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }

                )
                Switch(
                    checked = reviewCheckedState.value,
                    onCheckedChange = {
                        reviewCheckedState.value = it
                        UserSharedPreference(App.context()).setNoticePref("review", chatCheckedState.value)
                    },
                    modifier = Modifier.constrainAs(switch){
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.backgroundGray)
            ) {
                Text(text = "저장 용량 관리", Modifier.padding(10.dp), color = Color.DarkGray, fontSize = 13.sp)
            }
            Surface(modifier = Modifier.fillMaxWidth()
                .clickable {

                }
            ) {
                Text(
                    text = "디바이스 내 채팅 내역 삭제", fontSize = 16.sp,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                )
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.backgroundGray)
            ) {
                Text(text = "앱 정보", Modifier.padding(10.dp), color = Color.DarkGray, fontSize = 13.sp)
            }
            Text(
                text = version,
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(20.dp)
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.backgroundGray)
            ) {
                Text(text = "계정관리", Modifier.padding(10.dp), color = Color.DarkGray, fontSize = 13.sp)
            }
            Surface(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    mainViewModel.toStartActivity()
                }
            ){
                Text(
                    text = "로그아웃",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 15.dp)
                        .fillMaxWidth()
                )
            }
            Surface(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    mainViewModel.toStartActivity()
                }
            ){
                Text(
                    text = "계정 탈퇴",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(top = 15.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                        .fillMaxWidth()

                )
                //LogoutButton(loginViewModel, mainViewModel)
            }}
        }
    }
}
//
//@Composable
//fun LogoutButton(viewModel: LoginViewModel, mainViewModel: MainViewModel) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 40.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val context = LocalContext.current
//        Button(onClick = {
//            // viewModel.signOut()
//            // context.startActivity(Intent(context, StartActivity::class.java))
//            mainViewModel.toStartActivity()
//        }) {
//            Text(text = "로그아웃")
//        }
//    }
//}}
//
//@Composable
//fun Switch(
//    width: Dp = 45.dp,
//    height: Dp = 25.dp,
//    checkedTrackColor: Color = colorResource(id = R.color.green),
//    uncheckedTrackColor: Color = colorResource(id = R.color.green),
//    gapBetweenThumbAndTrackEdge: Dp = 8.dp,
//    borderWidth: Dp = 2.dp,
//    cornerSize: Int = 50,
//    iconInnerPadding: Dp = 3.dp,
//    thumbSize: Dp = 15.dp,
//) {
//
//    // this is to disable the ripple effect
//    val interactionSource = remember {
//        MutableInteractionSource()
//    }
//
//    // state of the switch
//    var switchOn by remember {
//        mutableStateOf(true)
//    }
//
//    // for moving the thumb
//    val alignment by animateAlignmentAsState(if (switchOn) 1f else -1f)
//
//    // outer rectangle with border
//    Row() {
//        Box(
//            modifier = Modifier
//                .size(width = width, height = height)
//                .border(
//                    width = borderWidth,
//                    color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
//                    shape = RoundedCornerShape(percent = cornerSize)
//                )
//                .clickable(
//                    indication = null,
//                    interactionSource = interactionSource
//                ) {
//                    switchOn = !switchOn
//                },
//            contentAlignment = Alignment.Center
//        ) {
//
//            // this is to add padding at the each horizontal side
//            Box(
//                modifier = Modifier
//                    .padding(
//                        start = gapBetweenThumbAndTrackEdge,
//                        end = gapBetweenThumbAndTrackEdge
//                    )
//                    .fillMaxSize(),
//                contentAlignment = alignment
//            ) {
//
//                // thumb with icon
//                Icon(
//                    imageVector = if (switchOn) Icons.Filled.Done else Icons.Filled.Close,
//                    contentDescription = if (switchOn) "Enabled" else "Disabled",
//                    modifier = Modifier
//                        .size(size = thumbSize)
//                        .background(
//                            color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
//                            shape = CircleShape
//                        )
//                        .padding(all = iconInnerPadding),
//                    tint = Color.White
//                )
//            }
//        }
//        Spacer(modifier = Modifier.width(5.dp))
//    Text(text = if (switchOn) "ON" else "OFF")
//    }
//}