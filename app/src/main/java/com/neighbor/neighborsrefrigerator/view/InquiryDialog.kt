package com.neighbor.neighborsrefrigerator.view

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neighbor.neighborsrefrigerator.scenarios.main.MainActivity
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel

@Composable
fun InquiryDialog(viewModel: MainViewModel, onChangeState: () -> Unit) {
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
                    viewModel.sendEmail(inquiryContent, userEmail)

                }) {
                Text(text = "문의")
            }
        }
    )
}
