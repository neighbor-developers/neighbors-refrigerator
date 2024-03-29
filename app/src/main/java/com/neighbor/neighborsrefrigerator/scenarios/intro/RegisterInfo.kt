package com.neighbor.neighborsrefrigerator.scenarios.intro

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.view.RegisterDialog
import com.neighbor.neighborsrefrigerator.view.SearchAddressDialog
import com.neighbor.neighborsrefrigerator.viewmodels.LoginViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "FlowOperatorInvokedInComposition")
@Composable
fun RegisterInfo(
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavHostController
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val dialogState = remember {
            mutableStateOf(false)
        }
        if (dialogState.value){
            RegisterDialog {
                dialogState.value = false
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "이웃집 냉장고", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(60.dp))
        GetNickname(loginViewModel)
        GetMainAddress(loginViewModel)

        Spacer(modifier = Modifier.height(60.dp))
        TextButton(
            onClick = {
                if (loginViewModel.availableNickname.value && loginViewModel.fillAddressMain.value){
                    loginViewModel.registerPersonDB()
                    navController.navigate("Guide")
                }else{
                    dialogState.value = true
                }
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        )
        {
            Text (text = "가입하기", modifier = Modifier
                .fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 15.sp, color = Color.White)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GetNickname(viewModel:LoginViewModel) {
    val availableNickname = viewModel.availableNickname.collectAsState()
    // remember: 상태를 가지고 있음
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            TextField(
                value = viewModel.userNicknameInput,
                onValueChange = { viewModel.userNicknameInput = it },
                placeholder = { Text("닉네임") },
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
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    cursorColor = Color.DarkGray
                ),
                modifier = Modifier.weight(17f)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                modifier = Modifier.weight(5f),
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.checkNickname()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                content = {Text(text = "중복체크", fontSize = 12.sp)},
                contentPadding = PaddingValues(7.dp)
            )
        }
        if (viewModel.isOverlap.value){
            Text(text = "닉네임은 중복될 수 없습니다", color = Color.Gray, textAlign = TextAlign.Right, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        }
        if (viewModel.isEmpty.value){
            Text(text = "닉네임을 한글자 이상 입력해주세요", color = Color.Gray, textAlign = TextAlign.Right, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        }
    }
}

@Composable
fun GetMainAddress(viewModel: LoginViewModel) {
    var dialogState by remember { mutableStateOf(false) }
    val searchAddressDialogViewModel by remember{
        mutableStateOf(SearchAddressDialogViewModel())
    }
    var address = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        SearchAddressDialog(
            dialogState = dialogState,
            onConfirm = {
                viewModel.fillAddressMain.value = address.value.isNotEmpty()
                dialogState = false
            },
            onDismiss = { dialogState = false },
            viewModel = searchAddressDialogViewModel,
            changeAddress = {
                viewModel.addressMain.value = it
                address.value = it
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth().clickable{
                dialogState = true
            },
            value = address.value,
            onValueChange = { address.value = it },
            placeholder = { Text("집 주소") },
            singleLine = true,
            enabled = false,
            leadingIcon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { dialogState = true }) {
                    // Icon(imageVector = cons.Default.Search, cntentDescription = null)
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.Transparent
            )
        )
        Text(text = "동까지 입력해주세요", color = Color.Gray, textAlign = TextAlign.Right, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))

        val addressDetail = remember {
            mutableStateOf("")
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = addressDetail.value,
            onValueChange = {
                addressDetail.value = it
                viewModel.addressDetail.value = it
                            },
            placeholder = { Text("상세주소를 작성해 주세요") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                cursorColor = Color.DarkGray
            )
        )
    }
}