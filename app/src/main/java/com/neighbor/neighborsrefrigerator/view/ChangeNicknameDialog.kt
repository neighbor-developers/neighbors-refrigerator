package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
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
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChangeNicknameDialog(changeDialogState:(Boolean) -> Unit, viewModel: MainViewModel) {
    val txtFieldError = remember { mutableStateOf("") }
    val nickname = remember { mutableStateOf("") }
    var hasNickname by remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = { changeDialogState(false) }) {
        Surface(
            shape = RoundedCornerShape(5.dp),
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
                            fontSize = 14.sp,
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

                    Spacer(modifier = Modifier.height(15.dp))

                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = colorResource(id = R.color.green)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
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


                    if (hasNickname){
                        Text(text = "중복된 닉네임이 있습니다.", color = Color.DarkGray, fontSize = 12.sp)
                    }else{
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    if (txtFieldError.value.isNotEmpty()){
                        Text(text = txtFieldError.value, color = Color.DarkGray, fontSize = 12.sp)
                    }else{
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                if (nickname.value.isEmpty()) {
                                    txtFieldError.value = "빈칸은 입력하실 수 없습니다."
                                    return@Button
                                } else {
                                    CoroutineScope(Dispatchers.Main).launch() {
                                        val result = viewModel.changeNickname(nickname.value)
                                        if (result){
                                            changeDialogState(false)
                                        }else{
                                            hasNickname = true
                                            txtFieldError.value = ""
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green)),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text(text = "변경", color = Color.White, textAlign = TextAlign.Center)
                        }
                    }

                }
            }
        }
    }
}
