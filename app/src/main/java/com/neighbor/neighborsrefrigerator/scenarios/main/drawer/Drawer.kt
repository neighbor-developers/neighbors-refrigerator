package com.neighbor.neighborsrefrigerator.scenarios.main.drawer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.scenarios.intro.SearchAddressDialog
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.RegisterInfoViewModel

data class DrawerItem(
    val route: NAV_ROUTE,
    val name: String
)

private val drawerMenu = listOf(
    DrawerItem(NAV_ROUTE.SETTING, "설정"),
    DrawerItem(NAV_ROUTE.TRADE_HISTORY, "거래 내역"),
)

@Composable
fun Drawer(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var flowerDialogState by remember { mutableStateOf(false) }
    var locationDialogState by remember { mutableStateOf(false) }
    val showNicknameDialog = remember { mutableStateOf(false) }
    val flowerNum = remember { mutableStateOf(1) }

    Surface{
        Column(
            modifier.padding(20.dp)
        ) {
            if (flowerDialogState)
                FlowerDialog({flowerDialogState = false}, flowerNum)

            if (locationDialogState)
                SearchAddressDialog(
                    dialogState = locationDialogState,
                    onDismiss = {locationDialogState = false},
                    viewModel = RegisterInfoViewModel()
                )
            Button(
                onClick = {
                    flowerDialogState = true
                },
                modifier
                    .size(130.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = Color.LightGray,
                        shape = CircleShape
                    ),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Image(
                    painter = painterResource(
                        when (flowerNum.value) {
                            1 -> R.drawable.sprout
                            2 -> R.drawable.sprout2
                            3 -> R.drawable.sprout3
                            else -> R.drawable.sprout
                        }
                    ),
                    contentDescription = "App icon"
                )
            }

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (showNicknameDialog.value){
                    CustomDialog(value = "",
                        changeDialogState = { showNicknameDialog.value = it },
                        setValue = {})
                }
                Text("Nickname", fontSize = 25.sp)
                IconButton(
                    modifier = Modifier.then(Modifier.size(30.dp)),
                    onClick = {
                        showNicknameDialog.value = true
                    }
                ) {
                    Icon(Icons.Filled.Settings, "contentDescription", tint = Color.Gray)
                }
            }
            Text(
                text = "내 위치 설정",
                style = MaterialTheme.typography.h4,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { locationDialogState = true }
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            drawerMenu.forEach { menu ->
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.h4,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable { navController.navigate(menu.route.routeName) }
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
            }
        }
    }
}

data class Flower(
    val id: Int,
    val resource : Int
)

private val flowerList = listOf(
    Flower(1, R.drawable.sprout),
    Flower(2, R.drawable.sprout2),
    Flower(3, R.drawable.sprout3)
)

@Composable
fun FlowerDialog(changeDialogState : () -> Unit, flowerNum : MutableState<Int>) {

    Dialog(onDismissRequest = { changeDialogState() }) {
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
                                .clickable { changeDialogState() }
                        )
                    }

                    Column(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        flowerList.forEach { flower ->
                            Button(
                                onClick = {
                                    changeDialogState()
                                    flowerNum.value = flower.id
                                },
                                shape = CircleShape,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(top = 10.dp)
                                    .border(
                                        width = 3.dp,
                                        color = Color.LightGray,
                                        shape = CircleShape
                                    ),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                            ){
                                Image(painter = painterResource(id = flower.resource), contentDescription = "꽃")
                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CustomDialog(value: String, changeDialogState:(Boolean) -> Unit, setValue: (String) -> Unit) {
    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { changeDialogState(false) }) {
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
                                fontSize = 18.sp,
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
                                .clickable { changeDialogState(false) }
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
                                }else {
                                    setValue(txtField.value)
                                    changeDialogState(false)
                                }
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
