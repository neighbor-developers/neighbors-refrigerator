package com.neighbor.neighborsrefrigerator.scenarios.main.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalLevel
import com.neighbor.neighborsrefrigerator.utilities.UseGeocoder
import com.neighbor.neighborsrefrigerator.view.*
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel

private val drawerMenu = listOf("내 정보", "내 위치 바꾸기", "설정", "문의하기")

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
    val dbAccessModule by remember {
        mutableStateOf(DBAccessModule())
    }
    val searchAddressDialogViewModel by remember{
        mutableStateOf(SearchAddressDialogViewModel())
    }

    var postData : List<PostData> by remember {
        mutableStateOf(arrayListOf())
    }

    val nickname = UserSharedPreference(App.context()).getUserPrefs("nickname")
    val flowerVer = UserSharedPreference(App.context()).getLevelPref("flowerVer")
    val id = UserSharedPreference(App.context()).getUserPrefs("id")!!.toInt()
    val calLevel = CalLevel()
    val level = calLevel.GetUserLevel(postData)

    var locationDialogState by remember { mutableStateOf(false) }
    var inquiryDialogState by remember { mutableStateOf(false) }


    Column(modifier.padding(20.dp))
    {
        if (locationDialogState)
            SearchAddressDialog(
                dialogState = locationDialogState,
                onConfirm = { locationDialogState = false}, //  나중에 제대로 고치기
                onDismiss = {locationDialogState = false},
                viewModel = searchAddressDialogViewModel,
                changeAddress = {
                    val coordinateData: LatLng = UseGeocoder().addressToLatLng(it)
                    dbAccessModule.updateUserLocation(id, coordinateData.latitude, coordinateData.longitude, it)
                    }
            )
        if (inquiryDialogState)
            InquiryDialog(viewModel) {
                inquiryDialogState = false
            }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp, top = 20.dp)
            , verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = {
                },
                modifier.size(100.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, disabledBackgroundColor = Color.White),
                enabled = false
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(
                        when(level) {
                            2 ->
                            when (flowerVer) {
                                1 -> R.drawable.level2_ver1
                                2 -> R.drawable.level2_ver2
                                3 -> R.drawable.level2_ver3
                                else -> R.drawable.level1
                            }
                            3 ->
                                when(flowerVer){
                                    1 -> R.drawable.level3_ver1
                                    2 -> R.drawable.level3_ver2
                                    3 -> R.drawable.level3_ver3
                                    else -> R.drawable.level1
                                }
                            4 ->
                                when(flowerVer){
                                    1 -> R.drawable.level4_ver1
                                    2 -> R.drawable.level4_ver2
                                    3 -> R.drawable.level4_ver3
                                    else -> R.drawable.level1
                                }
                            else -> R.drawable.level1
                        }
                    ),
                    contentDescription = "level icon"
                )
            }
            Column(modifier = Modifier.padding(20.dp))
            {
                Text(nickname.toString(), fontSize = 17.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = email.toString(), fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(start = 3.dp))
            }
        }
        drawerMenu.forEach { menu ->
            when(menu){
                "내 정보" -> DrawerItem(menu = menu, click = { navController.navigate("${NAV_ROUTE.TRADE_HISTORY.routeName}/${id!!}") })
                "내 위치 바꾸기" -> DrawerItem(menu = menu, click = { locationDialogState = true })
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
        Row(modifier = Modifier
            .padding(start = 10.dp, bottom = 20.dp, top = 20.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                imageVector = when(menu){
                    "내 정보" -> Icons.Filled.Person
                    "내 위치 바꾸기" -> Icons.Filled.LocationOn
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