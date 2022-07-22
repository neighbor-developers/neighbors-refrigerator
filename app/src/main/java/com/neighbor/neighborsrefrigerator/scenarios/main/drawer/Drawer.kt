package com.neighbor.neighborsrefrigerator.scenarios.main.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.ui.theme.NeighborsRefrigeratorTheme

sealed class DrawerScreens(val title: String, val route: String) {
    object Home : DrawerScreens("홈", "home")
    object Transaction : DrawerScreens("거래 내역 조회", "transaction")
    object LocationSetting : DrawerScreens("내 동네 설정", "locationSetting")
    object Setting : DrawerScreens("환경 설정", "setting")
}

private val screens = listOf(
    DrawerScreens.Home,
    DrawerScreens.Transaction,
    DrawerScreens.LocationSetting,
    DrawerScreens.Setting
)

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val showNicknameDialog = remember { mutableStateOf(false) }
    val flowerNum = remember {
        mutableStateOf(1)
    }

    Surface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(top = 48.dp)
            ) {
                if (showDialog.value)
                    flowerDialog(
                        setShowDialog = { showDialog.value = it },
                        flowerNum)
                Button(
                    onClick = {
                        showDialog.value = true
                    },
                    modifier
                        .size(165.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .padding(0.dp)
                        .border(
                            width = 3.dp,
                            color = Color.LightGray,
                            shape = CircleShape
                        ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Image(
                        painter = painterResource(
                        when(flowerNum.value){
                            1 -> R.drawable.sprout
                            2 -> R.drawable.sprout2
                            3 -> R.drawable.sprout3
                            else -> R.drawable.sprout
                        }),
                        contentDescription = "App icon"
                    )
                }

            }

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (showNicknameDialog.value)
                    CustomDialog(value = "", setShowDialog = {
                        showNicknameDialog.value = it
                    }) {}
                Text(
                    "Nickname", fontSize = 25.sp
                )
                IconButton(modifier = Modifier.then(Modifier.size(30.dp)),
                    onClick = {
                        showNicknameDialog.value = true
                    }) {
                    Icon(
                        Icons.Filled.Settings,
                        "contentDescription",
                        tint = Color.Gray
                    )
                }
            }

            screens.forEach { screen ->
                Spacer(Modifier.height(24.dp))
                modifier
                    .padding(top = 10.dp)

                Text(
                    text = screen.title,
                    style = MaterialTheme.typography.h4,
                    fontSize = 33.sp,
                    modifier = Modifier
                        .clickable {
                            onDestinationClicked(screen.route)
                        }
                        .padding(start = 24.dp)
                )
            }
        }
    }
}


@Composable
fun flowerDialog(setShowDialog: (Boolean) -> Unit, flowerNum : MutableState<Int>) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "(닉네임)님이 피우실 꽃을 선택해주세요.",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "",
                                tint = colorResource(android.R.color.darker_gray),
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clickable { setShowDialog(false) }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Column() {
                                Button(
                                    onClick = {
                                        setShowDialog(false)
                                    },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .border(
                                            width = 3.dp,
                                            color = Color.LightGray,
                                            shape = CircleShape
                                        ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                                ) {
                                    flowerNum.value = 1
                                }

                                Button(
                                    onClick = {
                                        setShowDialog(false)
                                    },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .border(
                                            width = 3.dp,
                                            color = Color.LightGray,
                                            shape = CircleShape
                                        ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                                ) {
                                    flowerNum.value = 2
                                }

                                Button(
                                    onClick = {
                                        setShowDialog(false)
                                    },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .border(
                                            width = 3.dp,
                                            color = Color.LightGray,
                                            shape = CircleShape
                                        ),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                                ) {
                                    flowerNum.value = 3
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val txtFieldError = remember { mutableStateOf("") }
        val txtField = remember { mutableStateOf(value) }

        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "원하는 닉네임을 작성해주십시오.",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "",
                                tint = colorResource(android.R.color.darker_gray),
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clickable { setShowDialog(false) }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(
                                        width = 2.dp,
                                        color = colorResource(id = if (txtFieldError.value.isEmpty()) android.R.color.holo_green_light else android.R.color.holo_red_dark)
                                    ),
                                    shape = RoundedCornerShape(50)
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "",
                                    tint = colorResource(android.R.color.holo_green_light),
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )
                            },
                            placeholder = { Text(text = "ex. 정왕동땅부자") },
                            value = txtField.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                txtField.value = it.take(10)
                            })

                        Spacer(modifier = Modifier.height(25.dp))

                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Button(
                                onClick = {
                                    if (txtField.value.isEmpty()) {
                                        txtFieldError.value = "빈칸은 입력하실 수 없습니다."
                                        return@Button
                                    }
                                    setValue(txtField.value)
                                    setShowDialog(false)
                                },
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .border(
                                        width = 3.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(50.dp)
                                    ),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                            ) {
                                Text(text = "입력")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerPreview() {
    NeighborsRefrigeratorTheme {
        Drawer {}
    }
}
