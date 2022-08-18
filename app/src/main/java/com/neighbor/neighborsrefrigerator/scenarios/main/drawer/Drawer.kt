package com.neighbor.neighborsrefrigerator.scenarios.main.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.view.CustomDialog
import com.neighbor.neighborsrefrigerator.view.FlowerDialog
import com.neighbor.neighborsrefrigerator.view.InquiryDialog
import com.neighbor.neighborsrefrigerator.view.SearchAddressDialog
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel

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
    val viewModel = MainViewModel()

    var flowerDialogState by remember { mutableStateOf(false) }
    var locationDialogState by remember { mutableStateOf(false) }
    var inquiryDialogState by remember { mutableStateOf(false) }
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
                    onConfirm = { locationDialogState = false}, //  나중에 제대로 고치기
                    onDismiss = {locationDialogState = false},
                    viewModel = SearchAddressDialogViewModel()
                )
            if (inquiryDialogState)
                InquiryDialog(viewModel) {
                    inquiryDialogState = false
                }
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
            Text(
                text = "문의하기",
                style = MaterialTheme.typography.h4,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { inquiryDialogState = true }
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