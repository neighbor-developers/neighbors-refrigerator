package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.utilities.App

data class Flower(
    val id: Int,
    val resource : Int
)

private val flowerList = listOf(
    Flower(1, R.drawable.level4_ver1),
    Flower(2, R.drawable.level4_ver2),
    Flower(3, R.drawable.level4_ver3)
)

@Composable
fun FlowerDialog(changeDialogState : () -> Unit) {

    val nickname =UserSharedPreference(App.context()).getUserPrefs("nickname")
    val ver = UserSharedPreference(App.context()).getLevelPref("flowerVer")

    val selectedValue = remember { mutableStateOf(ver) }
    val isSelectedItem: (Int) -> Boolean = { selectedValue.value == it }
    val onChangeState: (Int) -> Unit = { selectedValue.value = it }
    AlertDialog(
        onDismissRequest = { changeDialogState() },
        title = { Text(text = "${nickname}님이 피우실 꽃을 선택해주세요")},
        text = {


            Row(modifier = Modifier.fillMaxWidth()) {
                flowerList.forEach { item ->
                    Column(
                        modifier = Modifier
                            .selectable(
                                selected = isSelectedItem(item.id),
                                onClick = { onChangeState(item.id) },
                                role = Role.RadioButton
                            )
                            .padding(top = 10.dp, bottom = 10.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RadioButton(
                            selected = isSelectedItem(item.id),
                            onClick = null,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .size(1.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Image(
                            painter = painterResource(id = item.resource),
                            contentDescription = "",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { changeDialogState() }) {
                Text(text = "취소", color = Color.Black)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    UserSharedPreference(App.context()).setLevelPref("flowerVer", selectedValue.value)
                    changeDialogState()
                    // 벌점 맥이기
                }) {
                Text(text = "변경", color = Color.Black)
            }
        }
    )
}
