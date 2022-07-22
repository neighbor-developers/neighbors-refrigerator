package com.neighbor.neighborsrefrigerator.scenarios.intro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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


@Composable
fun RegisterInfo(){
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GetNickname()
            GetAddress()
        }
    }
}

@Composable
fun GetNickname() {
    // remember: 상태를 가지고 있음
    var userNicknameInput by remember { mutableStateOf(TextFieldValue()) }
    var availableNickname by remember { mutableStateOf(false) }
    val passwordResource: (Boolean) -> Int = {
        if (it) { // DB에서 Nickname 확인 후 존재한다면
            R.drawable.ic_check_green
        } else { //  DB에서 Nickname 확인 후 존재하지 않는다면
            R.drawable.ic_check_red
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = userNicknameInput,
            onValueChange = { newValue -> userNicknameInput = newValue },
            label = { Text("닉네임") },
            placeholder = { Text("작성해 주세요") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            trailingIcon = {
                // 참고: https://www.charlezz.com/?p=45513
                IconButton(onClick = { /*입력한 valule값을 DB에서 Nickname 확인*/ }) {
                    // Nickname 확인 후 존재하지 않는다면 등록 버튼 클릭 가능하게 하기
                    Icon(painter = painterResource(id = passwordResource(availableNickname/*.value */)), contentDescription = null)
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
fun GetAddress() {
    var userAddressInput by remember { mutableStateOf(TextFieldValue()) }
    var dialogState by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = userAddressInput,
            onValueChange = { newValue -> userAddressInput = newValue },
            label = { Text("집 주소") },

            placeholder = { Text("작성해 주세요") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { dialogState = true }) {
                    SearchAddressDialog(dialogState = dialogState, onDissmissRequest = {
                        dialogState = !it
                    })
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
    }
}

@Composable
fun SaveDataButton() {
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
            GetNickname()
            GetAddress()
        }
    }
}
