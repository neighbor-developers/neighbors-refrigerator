package com.neighbor.neighborsrefrigerator.scenarios.main

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.composable
import com.neighbor.neighborsrefrigerator.BuildConfig
import com.neighbor.neighborsrefrigerator.scenarios.intro.LoginScreen
import com.neighbor.neighborsrefrigerator.scenarios.intro.StartActivity
import com.neighbor.neighborsrefrigerator.scenarios.main.post.register.SharePostRegisterScreen
import com.neighbor.neighborsrefrigerator.viewmodels.LoginViewModel

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}

@Preview
@Composable
fun Setting() {
    val viewModel = LoginViewModel()
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp, 40.dp, 0.dp, 0.dp)
            ) {
                Row(modifier = Modifier.padding(0.dp, 25.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "채팅 알림",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily.Default
                        )
                    )
                    Switch()
                }
                Row(modifier = Modifier.padding(0.dp, 25.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "후기 작성 알림",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily.Default
                        )
                    )
                    Switch()
                }

                TextButton(onClick = { /*누르면 계정 삭제*/ }) {
                    Text(
                        text = "디바이스 내 채팅 내역 삭제",
                        style = TextStyle(
                            fontSize = 26.9.sp,
                            fontFamily = FontFamily.Default,
                        ),
                        color = Color.Black
                    )
                }

                val version =
                    "버전 정보 : " + BuildConfig.VERSION_NAME
                Text(
                    text = version,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Default
                    )
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                LogoutButton(viewModel)

                TextButton(onClick = { /*누르면 계정 삭제*/ }) {
                    Text(
                        text = "계정 탈퇴",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily.Default,
                        ),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutButton(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Button(onClick = {
            // viewModel.signOut()
            // context.startActivity(Intent(context, StartActivity::class.java))
        }) {
            Text(text = "로그아웃")
        }
    }
}

@Composable
fun Switch(
    width: Dp = 72.dp,
    height: Dp = 40.dp,
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color(0xFFe0e0e0),
    gapBetweenThumbAndTrackEdge: Dp = 8.dp,
    borderWidth: Dp = 4.dp,
    cornerSize: Int = 50,
    iconInnerPadding: Dp = 4.dp,
    thumbSize: Dp = 24.dp
) {

    // this is to disable the ripple effect
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // state of the switch
    var switchOn by remember {
        mutableStateOf(true)
    }

    // for moving the thumb
    val alignment by animateAlignmentAsState(if (switchOn) 1f else -1f)

    // outer rectangle with border
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .border(
                width = borderWidth,
                color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(percent = cornerSize)
            )
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                switchOn = !switchOn
            },
        contentAlignment = Alignment.Center
    ) {

        // this is to add padding at the each horizontal side
        Box(
            modifier = Modifier
                .padding(
                    start = gapBetweenThumbAndTrackEdge,
                    end = gapBetweenThumbAndTrackEdge
                )
                .fillMaxSize(),
            contentAlignment = alignment
        ) {

            // thumb with icon
            Icon(
                imageVector = if (switchOn) Icons.Filled.Done else Icons.Filled.Close,
                contentDescription = if (switchOn) "Enabled" else "Disabled",
                modifier = Modifier
                    .size(size = thumbSize)
                    .background(
                        color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
                        shape = CircleShape
                    )
                    .padding(all = iconInnerPadding),
                tint = Color.White
            )
        }
    }

    // gap between switch and the text
    Spacer(modifier = Modifier.height(height = 16.dp))

    Text(text = if (switchOn) "ON" else "OFF")
}