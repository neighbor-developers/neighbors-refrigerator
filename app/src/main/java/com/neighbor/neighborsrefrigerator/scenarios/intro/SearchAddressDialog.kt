package com.neighbor.neighborsrefrigerator.scenarios.intro

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.neighbor.neighborsrefrigerator.viewmodels.RegisterInfoViewModel

@Composable
fun SearchAddressDialog(
    dialogState: Boolean,
    onDissmissRequest: (dialogState: Boolean) -> Unit
) {
    if (dialogState) {
        AlertDialog(
            backgroundColor = Color.White,
            onDismissRequest = {
                onDissmissRequest(dialogState)
            },
            title = null,
            text = {
                Column() {
                    Text(
                        "찾으시려는 동(읍/면/리)과 번지수or건물명을 정확하게 입력해 주세요.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(),
                        fontSize = 16.sp,
                        lineHeight = 17.sp
                    )
                    DialogUI()
                }
            },
            buttons = {
            },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            shape = RoundedCornerShape(9.dp)
        )
    }
}

@Composable
fun DialogUI() {
    var textState by remember { mutableStateOf(TextFieldValue()) }

    Column {
        Spacer(
            modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
        )
        /*Divider(
            color = Color.DarkGray,
            thickness = 0.8.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )*/
        /*TextField(
            modifier = Modifier.padding(20.dp)
                .fillMaxWidth(),
            value = textState,
            onValueChange = { textFieldValue -> textState = textFieldValue },
            trailingIcon = {
                painterResource(id = R.drawable.ic_baseline_search_24)
            }
        )*/
        getAddressDetail()
        AddressList(state = false, addressList = arrayListOf())
    }
}

@Composable
fun getAddressDetail(){
    var userAddressInput by remember { mutableStateOf(TextFieldValue()) }
    var text:List<String> = arrayListOf()

    OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = userAddressInput,
            onValueChange = { newValue -> userAddressInput = newValue },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { RegisterInfoViewModel().setAddress(textField = userAddressInput.text, completion = {
                    text = RegisterInfoViewModel().sendAddress(it)
                })}) {
                    AddressList(state = true, addressList = text)
                    // Icon(imageVector = cons.Default.Search, cntentDescription = null)
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                    // 기본 테마 색 지정
                    backgroundColor = Color.White
            )
    )
}

// sharedPreference 사용 필요
@Composable
fun AddressList(state: Boolean, addressList: List<String>) {
    var listState by remember { mutableStateOf(state) }
    val list by remember { mutableStateOf(addressList) }
    if(listState){
        Log.d("실행", "true")
        LazyColumn() {
            itemsIndexed(
                list
            ) { index, item ->
                Text(
                    text = item
                    /* fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                    * */
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Surface() {
        var dialogState by remember {
            mutableStateOf(true)
        }

        SearchAddressDialog(dialogState = dialogState, onDissmissRequest = {
            dialogState = !it
        })
    }
}
