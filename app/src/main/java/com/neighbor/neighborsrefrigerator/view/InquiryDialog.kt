package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighbor.neighborsrefrigerator.R
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
                TextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    placeholder = { Text(text = "답변을 받을 이메일을 입력해주세요", fontSize = 13.sp)},
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "email")},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        leadingIconColor = colorResource(id = R.color.green)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = inquiryContent,
                    onValueChange = { inquiryContent = it },
                    placeholder = { Text(text = "문의 사항을 입력해주세요.", fontSize = 13.sp)},
                    singleLine = false,
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "문의 내용")},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        leadingIconColor = colorResource(id = R.color.green),
                        focusedIndicatorColor = colorResource(id = R.color.green)
                    )
                )


            }
        },
        dismissButton = {
            Button(
                onClick = { onChangeState() },
                border = BorderStroke(1.3.dp, colorResource(id = R.color.green)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "취소", color = colorResource(id = R.color.green))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onChangeState()
                    inquiryContent = userEmail + "\n" + inquiryContent
                    viewModel.sendEmail(0, inquiryContent)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                elevation = ButtonDefaults.elevation(0.dp)

            ) {
                Text(text = "문의", color = Color.White)
            }

        }
    )
}
