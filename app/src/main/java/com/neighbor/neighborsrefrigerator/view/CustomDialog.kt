package com.neighbor.neighborsrefrigerator.view

import android.R
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(value: String, changeDialogState:(Boolean) -> Unit, setValue: (String) -> Unit) {
    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { changeDialogState(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "원하는 닉네임을 작성해주십시오.",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = colorResource(R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { changeDialogState(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    width = 2.dp,
                                    color = colorResource(id = if (txtFieldError.value.isEmpty()) R.color.holo_green_light else R.color.holo_red_dark)
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
                                tint = colorResource(R.color.holo_green_light),
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = "ex. 정왕동땅부자") },
                        value = txtField.value,
                        onValueChange = {
                            txtField.value = it.take(10)
                        })

                    Spacer(modifier = Modifier.height(25.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "빈칸은 입력하실 수 없습니다."
                                    return@Button
                                }else {
                                    setValue(txtField.value)
                                    changeDialogState(false)
                                }
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .border(
                                    width = 3.dp,
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(50.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text(text = "입력")
                        }
                    }
                }
            }
        }
    }
}
