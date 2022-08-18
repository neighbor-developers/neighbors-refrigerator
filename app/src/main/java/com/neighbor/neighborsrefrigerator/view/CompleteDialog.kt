package com.neighbor.neighborsrefrigerator.view


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.neighbor.neighborsrefrigerator.data.PostData

@Composable
fun CompleteDialog(type: String, onChangeState: () -> Unit, completeTrade: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onChangeState() },
        title = {
            Text(
                text = "완료",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column {
                if(type == "거래"){
                    Text(text = "거래를 완료하시겠습니까?")
                }else{
                    Text(text = "등록하시겠습니까?")
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
                    completeTrade()
                }) {
                Text(text = "확인")
            }
        }
    )
}
