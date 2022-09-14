package com.neighbor.neighborsrefrigerator.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel

@Composable
fun SearchAddressDialog(
    dialogState: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    changeAddress: (String) -> Unit,
    viewModel: SearchAddressDialogViewModel
) {
    // https://blog.logrocket.com/adding-alertdialog-jetpack-compose-android-apps/
    if (dialogState) {
        AlertDialog(
            backgroundColor = Color.White,
            onDismissRequest = { onDismiss() },
            title = {Text(text = "위치 설정", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 16.sp)},
            text = {
                Column(){
                    DialogUI(viewModel, changeAddress)
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() },
                    border = BorderStroke(1.3.dp, colorResource(id = R.color.green)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(text = "취소", color = colorResource(id = R.color.green))
                }
            },
            confirmButton = {
                Button(
                    onClick = { onConfirm() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                    elevation = ButtonDefaults.elevation(0.dp)

                ) {
                    Text(text = "확인", color = Color.White)
                }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            modifier = Modifier
                .padding(10.dp)
        )
    }
}

@Composable
fun DialogUI(viewModel: SearchAddressDialogViewModel, changeAddress: (String) -> Unit) {
    val address = remember {
        mutableStateOf("")
    }
    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = address.value,
            onValueChange = { address.value = it },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = "위치", tint = colorResource(
                id = R.color.green
            ))},
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.setAddress(textField = address.value)
                }) {
                    Icon(imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = colorResource(id = R.color.green))

                }
            },
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = colorResource(id = R.color.green),
                cursorColor = Color.DarkGray
            ),
        )
        Box(
            modifier = Modifier.height(100.dp)
        ) {
            AddressList(viewModel, changeAddress) { address.value = it}
        }
    }
}

// sharedPreference 사용 필요
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("RememberReturnType", "StateFlowValueCalledInComposition")
@Composable
fun AddressList(viewModel: SearchAddressDialogViewModel, changeAddress: (String) -> Unit, changeAddressText: (String) -> Unit) {
    val addressList = viewModel.addressList.collectAsState()    // 바로, 계속 변화 감지, 통로
    if (addressList.value.isNullOrEmpty()) {
        Text(
            "동(읍/면/리)과 번지수 또는 건물명을 정확하게 입력해 주세요.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            fontSize = 10.sp,
            lineHeight = 17.sp,
        )
    } else {
        addressList.value?.let { address ->
            LazyColumn(userScrollEnabled = true) {
                itemsIndexed(
                    address
                ) { _, item ->
                    Log.d("실행", "있음")
                    Card(onClick = {
                        viewModel.userAddressInput = item
                        changeAddress(item)
                        changeAddressText(item)
                    }) {
                        Text(text = item, modifier = Modifier.padding(5.dp), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
