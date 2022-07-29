package com.neighbor.neighborsrefrigerator.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.neighbor.neighborsrefrigerator.scenarios.intro.GetMainAddress
import com.neighbor.neighborsrefrigerator.scenarios.intro.GetNickname
import com.neighbor.neighborsrefrigerator.viewmodels.RegisterInfoViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SearchAddressDialogViewModel

@Composable
fun SearchAddressDialog(
    dialogState: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: SearchAddressDialogViewModel
) {
    // https://blog.logrocket.com/adding-alertdialog-jetpack-compose-android-apps/
    if (dialogState) {
        AlertDialog(
            backgroundColor = Color.White,
            onDismissRequest = onDismiss,
            title = null,
            text = {
                Column() {
                    Text(
                        "찾으시려는 동(읍/면/리)과 번지수or건물명을 정확하게 입력해 주세요.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        fontSize = 16.sp,
                        lineHeight = 17.sp
                    )
                    DialogUI(viewModel)
                }
            },
            buttons = {
                    Box(modifier = Modifier.padding()) {
                        Row(){
                            TextButton( onClick = onConfirm )
                            {
                                Text(text = "확인")
                            }
                            TextButton(onClick = onDismiss)
                            {
                                Text(text = "Calcel")
                            }
                        }
                    }
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            shape = RoundedCornerShape(9.dp),
            modifier = Modifier.height(400.dp)
        )
    }
}

@Composable
fun DialogUI(viewModel: SearchAddressDialogViewModel) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
        )
        /*Divider(
            color = Color.DarkGray,
            thickness = 0.8.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )*/
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = viewModel.userAddressInput,
            onValueChange = { viewModel.userAddressInput = it },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.setAddress(textField = viewModel.userAddressInput)
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)

                }
            },
            colors = TextFieldDefaults.textFieldColors(
                // 기본 테마 색 지정
                backgroundColor = Color.White
            )
        )
        AddressList(viewModel)
    }
}

// sharedPreference 사용 필요
@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("RememberReturnType", "StateFlowValueCalledInComposition")
@Composable
fun AddressList(viewModel: SearchAddressDialogViewModel){
    val addressList = viewModel.addressList.collectAsState()    // 바로, 계속 변화 감지, 통로
    addressList.value?.let { address ->
        LazyColumn(userScrollEnabled = true) {
            itemsIndexed(
                address
            ) { index, item ->
                Log.d("실행", "있음")
                Card(onClick = {
                    viewModel.userAddressInput = item
                }) {
                    Text(text = item, modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            var dialogState by remember { mutableStateOf(false) }
            var searchAddressDialogViewModel = SearchAddressDialogViewModel()

            SearchAddressDialog(
                dialogState = dialogState,
                onConfirm = {
                    dialogState = false
                },
                onDismiss = { dialogState = false },
                viewModel = searchAddressDialogViewModel
            )
        }
    }
}
*/
