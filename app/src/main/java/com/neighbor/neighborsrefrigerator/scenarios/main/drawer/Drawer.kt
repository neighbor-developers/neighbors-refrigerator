package com.neighbor.neighborsrefrigerator.scenarios.main.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.view.*
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel

private val drawerMenu = listOf("내 위치 바꾸기", "거래내역", "설정", "문의하기")

@Composable
fun Drawer(
    viewModel: MainViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val auth = remember {
     mutableStateOf(FirebaseAuth.getInstance())
    }
    val email by remember {
        mutableStateOf(auth.value.currentUser?.email)
    }
    val nickname by remember {
        mutableStateOf(UserSharedPreference(App.context()).getUserPrefs("nickname"))
    }
    var flowerDialogState by remember { mutableStateOf(false) }
    var locationDialogState by remember { mutableStateOf(false) }
    var inquiryDialogState by remember { mutableStateOf(false) }
    var showNicknameDialog by remember { mutableStateOf(false) }
    val flowerNum = remember { mutableStateOf(1) }

    Column(modifier.padding(20.dp))
    {
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
        if (showNicknameDialog){
            ChangeNicknameDialog(
                changeDialogState = { showNicknameDialog = it },
                changeNickname = {
                    viewModel.changeNickname(it) })
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp, top = 20.dp)
            , verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = {
                    flowerDialogState = true
                },
                modifier.size(100.dp),
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
                    contentDescription = "level icon"
                )
            }
            Column(modifier = Modifier.padding(20.dp))
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(nickname.toString(), fontSize = 17.sp)
                    IconButton(
                        modifier = Modifier.padding(start = 8.dp).size(20.dp),
                        onClick = {
                            showNicknameDialog = true
                        }
                    ) {
                        Icon(Icons.Filled.Settings, "contentDescription", tint = colorResource(id = R.color.green))
                    }

                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = email.toString(), fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(start = 3.dp))
            }
        }
        drawerMenu.forEach { menu ->
            when(menu){
                "내 위치 바꾸기" -> DrawerItem(menu = menu, click = { locationDialogState = true })
                "거래내역" -> DrawerItem(menu = menu, click = { navController.navigate(NAV_ROUTE.TRADE_HISTORY.routeName) })
                "설정" -> DrawerItem(menu = menu, click = { navController.navigate(NAV_ROUTE.SETTING.routeName) })
                "문의하기" -> DrawerItem(menu = menu, click = { inquiryDialogState = true })
                else -> {}
            }
        }
    }

}

@Composable
fun DrawerItem(menu : String, click: () -> Unit){
    Surface(modifier = Modifier.clickable { click() }) {
        Row(modifier = Modifier.padding(start = 10.dp, bottom = 20.dp, top = 20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                imageVector = when(menu){
                    "내 위치 바꾸기" -> Icons.Filled.Home
                    "거래 내역" -> Icons.Filled.Menu
                    "설정" -> Icons.Filled.Settings
                    "문의하기" -> Icons.Filled.MailOutline
                    else -> Icons.Filled.Menu },
                contentDescription = "설정 메뉴",
                tint = colorResource(id = R.color.green),
                modifier = Modifier.size(20.dp)
            )
            Text(text = menu, fontSize = 18.sp, modifier = Modifier.padding(start = 50.dp))
        }
    }
}