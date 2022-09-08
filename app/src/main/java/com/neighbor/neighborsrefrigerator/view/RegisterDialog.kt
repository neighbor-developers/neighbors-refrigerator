package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun RegisterDialog(onChangeState: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onChangeState() },
        title = {
            Text(
                text = "정보 등록",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(text = "닉네임(중복체크)과 주소 모두 입력해주세요 "
                    ,textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeState()
                }) {
                Text(text = "확인", color = Color.DarkGray)
            }
        }
    )
}
