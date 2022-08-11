package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InquiryDialog(onChangeState: () -> Unit) {
    var userEmail by remember {
        mutableStateOf("")
    }
    var inquiryContent by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = { onChangeState() },
        title = { Text(text = "문의", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 25.dp)) {
                    Text(text = "이메일", modifier = Modifier.padding(bottom = 5.dp, end = 7.dp))
                    TextField(value = userEmail, onValueChange = { userEmail = it })
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "문의 내용", modifier = Modifier.padding(bottom = 5.dp, end = 7.dp))
                    TextField(value = inquiryContent, onValueChange = { inquiryContent = it })
                }

            }
        },
        dismissButton = {
            TextButton(onClick = { onChangeState() }) {
                Text(text = "취소")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeState()
                    // 벌점 맥이기
                }) {
                Text(text = "문의")
            }
        }
    )

}
