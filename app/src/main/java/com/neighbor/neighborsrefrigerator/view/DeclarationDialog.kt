package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.viewmodels.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DeclarationDialog(postId : Int, type: Int, onChangeState: () -> Unit, mainViewModel: MainViewModel = viewModel()) {
    val declarations = when(type){
        1 -> listOf("상업적 게시물(광고, 판매업자)", "나눔 금지 물품(건강 기능 식품 등)", "기타")
        2 -> listOf("부적절한 언어 사용(욕설, 비속어)", "거래 외 목적의 대화(사적 만남)", "게시물과 상이한 물품 전달", "기타")
        else -> listOf("")
    }
    val selectedValue = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onChangeState() },
        title = { Text(text = "신고", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        text = {
            Column {
                RadioButtons(declarations, selectedValue)
            }
               },
        dismissButton = {
            TextButton(onClick = { onChangeState() }) {
                Text(text = "취소", color = Color.Black)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    onChangeState()
                    selectedValue.value = FirebaseAuth.getInstance().currentUser!!.email + "\n" +  selectedValue.value
                    mainViewModel.sendEmail(postId, selectedValue.value)
                    // 벌점 맥이기
                }) {
                Text(text = "신고", color = Color.Black)
            }
        }
    )

}

@Composable
fun RadioButtons(declaration: List<String>, selectedValue: MutableState<String>){

    val isSelectedItem: (String) -> Boolean = {selectedValue.value == it}
    val onChangeState : (String) -> Unit = { selectedValue.value = it}


    Column(modifier = Modifier.padding(top = 10.dp)) {
        declaration.forEach { item ->
            Column {
                Row (
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedItem(item),
                            onClick = { onChangeState(item) },
                            role = Role.RadioButton
                        )
                        .padding(bottom = 3.dp)
                ){
                    RadioButton(
                        selected = isSelectedItem(item),
                        onClick = null,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = item, color = Color.DarkGray)
                }
                if (selectedValue.value == "기타" && item == "기타"){
                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = colorResource(id = R.color.green)
                    ),
                        value = selectedValue.value,
                        onValueChange = {selectedValue.value = it},
                        modifier = Modifier.height(30.dp)
                    )
                }
            }
        }
    }
}
