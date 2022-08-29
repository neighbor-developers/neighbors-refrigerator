package com.neighbor.neighborsrefrigerator.view


import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neighbor.neighborsrefrigerator.data.PostData

@Composable
fun CompleteDialog(type: String, onChangeState: () -> Unit, completeTrade: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onChangeState() },
        title = {
            Text(
                text = if (type == "취소") "취소" else "완료",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(text =
                    when(type){
                        "거래" -> "거래를 완료하시겠습니까?"
                        "취소" -> "입력중인 내용이 사라집니다."
                        else -> "등록하시겠습니까?"
                    },
                    textAlign = TextAlign.Center
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onChangeState() }) {
                Text(text = "취소", color = Color.DarkGray)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeState()
                    completeTrade()
                }) {
                Text(text = "확인", color = Color.DarkGray)
            }
        }
    )
}
