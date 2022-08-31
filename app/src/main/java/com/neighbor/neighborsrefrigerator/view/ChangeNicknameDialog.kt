package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.neighbor.neighborsrefrigerator.R

@Composable
fun ChangeNicknameDialog(changeDialogState:(Boolean) -> Unit, changeNickname: (String) -> Unit) {
    val txtFieldError = remember { mutableStateOf("") }
    val nickname = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { changeDialogState(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (text, button) = createRefs()
                        Text(
                            text = "닉네임 변경",
                            fontSize = 18.sp,
                            modifier = Modifier.constrainAs(text) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp)
                                .constrainAs(button) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .clickable { changeDialogState(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 1.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) R.color.green else R.color.green)
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "",
                                tint = colorResource(R.color.green),
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = "변경할 닉네임을 입력해주세요") },
                        value = nickname.value,
                        onValueChange = {
                            nickname.value = it.take(10)
                        })

                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        onClick = {
                            if (nickname.value.isEmpty()) {
                                txtFieldError.value = "빈칸은 입력하실 수 없습니다."
                                return@Button
                            } else {
                                changeNickname(nickname.value)
                                changeDialogState(false)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Text(text = "변경", color = colorResource(id = R.color.green), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }

                }
            }
        }
    }
}
