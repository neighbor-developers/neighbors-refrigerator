package com.neighbor.neighborsrefrigerator.scenarios.intro

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.view.SearchAddressDialog
import com.neighbor.neighborsrefrigerator.viewmodels.LoginViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.RegisterInfoViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "FlowOperatorInvokedInComposition")
@Composable
fun RegisterInfo(loginViewModel: LoginViewModel = viewModel()){
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var enabled = loginViewModel.buttonEnabled.collectAsState()
            var available = loginViewModel.availableNickname.collectAsState()
            //var enabled by remember { mutableStateOf(false) }
            //var enabled = registerInfoViewModel.buttonEnabled

            GetNickname(loginViewModel)
            GetMainAddress(loginViewModel)


            //registerInfoViewModel.check()

            TextButton(
                onClick = { loginViewModel.registerPersonDB() },
                enabled = enabled.value
            )
            {
                Text(text = "확인")
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GetNickname(viewModel:LoginViewModel) {
    val availableNickname = viewModel.availableNickname.collectAsState()
    // remember: 상태를 가지고 있음
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(){
            OutlinedTextField(
                value = viewModel.userNicknameInput,
                onValueChange = { viewModel.userNicknameInput = it },
                label = { Text("닉네임") },
                placeholder = { Text("작성해 주세요") },
                singleLine = true,
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                trailingIcon = {
                    // 아이콘 두 개 만들지 말고 하나로 색 변경하게 수정하기
                    if (availableNickname.value) { // DB에서 Nickname 확인 후 존재한다면
                        Icon(painter = painterResource(id = R.drawable.ic_check_green), tint = Color.Green, contentDescription = null)
                    }else { //  DB에서 Nickname 확인 후 존재하지 않는다면
                        Icon(painter = painterResource(id = R.drawable.ic_check_red), tint = Color.Red, contentDescription = null)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    // 기본 테마 색 지정
                    backgroundColor = Color.White
                )
            )
            OutlinedButton(
                modifier = Modifier
                    .padding(5.dp)
                    .wrapContentWidth(),
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.checkNickname()
                        viewModel.check()
                    }
                }){
                Text(text = "Check")
            }
        }
        Text(text = "닉네임은 중복될 수 없습니다", color = Color.Gray, textAlign = TextAlign.Right)
    }
}

@Composable
fun GetMainAddress(viewModel: LoginViewModel) {
    var dialogState by remember { mutableStateOf(false) }
    val searchAddressDialogViewModel by remember{
        mutableStateOf(SearchAddressDialogViewModel())
    }

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
            enabled = false,
            leadingIcon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { dialogState = true }) {
                    SearchAddressDialog(
                        dialogState = dialogState,
                        onConfirm = {
                            viewModel.addressMain = searchAddressDialogViewModel.userAddressInput
                            viewModel.fillAddressMain.value = viewModel.addressMain.isNotEmpty()
                            viewModel.buttonEnabled.value = viewModel.availableNickname.value && viewModel.fillAddressMain.value
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
