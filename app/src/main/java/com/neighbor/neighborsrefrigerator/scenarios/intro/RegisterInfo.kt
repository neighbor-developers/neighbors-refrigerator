package com.neighbor.neighborsrefrigerator.scenarios.intro

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import com.neighbor.neighborsrefrigerator.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.view.SearchAddressDialog
import com.neighbor.neighborsrefrigerator.viewmodels.LoginViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.RegisterInfoViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostRegisterViewModel


@Composable
fun RegisterInfo(navController: NavHostController){
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val registerInfoViewModel = RegisterInfoViewModel()

            GetNickname(registerInfoViewModel)
            GetMainAddress(registerInfoViewModel)
            TextButton( onClick = {
                registerInfoViewModel.registerPersonDB()
                navController.navigate(NAV_ROUTE.MAIN.routeName)
            })
            {
                Text(text = "확인")
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GetNickname(viewModel:RegisterInfoViewModel) {
    val availableNickname = viewModel.availableNickname.collectAsState()
    // remember: 상태를 가지고 있음
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.userNicknameInput,
            onValueChange = { viewModel.userNicknameInput = it },
            label = { Text("닉네임") },
            placeholder = { Text("작성해 주세요") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            trailingIcon = {
                // 참고: https://www.charlezz.com/?p=45513
                IconButton(onClick = { viewModel.checkNickname() }) {
                    // Nickname 확인 후 존재하지 않는다면 등록 버튼 클릭 가능하게 하기
                    if (availableNickname.value!!) { // DB에서 Nickname 확인 후 존재한다면
                        Icon(painter = painterResource(id = R.drawable.ic_check_green), tint = Color.Green, contentDescription = null)
                    }else { //  DB에서 Nickname 확인 후 존재하지 않는다면
                        Icon(painter = painterResource(id = R.drawable.ic_check_red), tint = Color.Red, contentDescription = null)
                    }
                    // Icon(painter = painterResource(id = passwordResource(viewModel.availableNickname.value!!)), tint = Color.Red, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.White
            )
        )
        Text(text = "닉네임은 중복될 수 없습니다", color = Color.Gray, textAlign = TextAlign.Right)
    }
}

@Composable
fun GetMainAddress(viewModel: RegisterInfoViewModel) {
    var dialogState by remember { mutableStateOf(false) }
    var searchAddressDialogViewModel = SearchAddressDialogViewModel()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.addressMain,
            onValueChange = { viewModel.addressMain = it },
            label = { Text("집 주소") },
            placeholder = { Text("작성해 주세요") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { dialogState = true }) {
                    SearchAddressDialog(
                        dialogState = dialogState,
                        onConfirm = {
                            viewModel.addressMain = searchAddressDialogViewModel.userAddressInput
                            dialogState = false
                        },
                        onDismiss = { dialogState = false },
                        viewModel = searchAddressDialogViewModel
                    )
                    // Icon(imageVector = cons.Default.Search, cntentDescription = null)
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.White
            )
        )
        Text(text = "동까지 입력해주세요", color = Color.Gray, textAlign = TextAlign.Right)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.addressDetail,
            onValueChange = { viewModel.addressDetail = it },
            label = { Text("상세 주소") },

            placeholder = { Text("작성해 주세요") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.White
            )
        )
        Text(text = "상세주소를 입력해주세요", color = Color.Gray, textAlign = TextAlign.Right)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            var viewModel = RegisterInfoViewModel()
            GetNickname(viewModel)
            GetMainAddress(viewModel = viewModel)

        }
    }
}
