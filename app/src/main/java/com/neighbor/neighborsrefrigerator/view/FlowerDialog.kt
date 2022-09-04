package com.neighbor.neighborsrefrigerator.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.utilities.App

data class Flower(
    val id: Int,
    val resource : Int
)

private val flowerList = listOf(
    Flower(1, R.drawable.sprout),
    Flower(2, R.drawable.sprout2),
    Flower(3, R.drawable.sprout3)
)

@Composable
fun FlowerDialog(changeDialogState : () -> Unit, flowerNum : MutableState<Int>) {

    val nickname =UserSharedPreference(App.context()).getUserPrefs("nickname")

    Dialog(onDismissRequest = { changeDialogState() }) {
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
                            text = "${nickname}님이 피우실 꽃을 선택해주세요.",
                            style = TextStyle(
                                fontSize = 15.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { changeDialogState() }
                        )
                    }

                    Column(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        flowerList.forEach { flower ->
                            Button(
                                onClick = {
                                    changeDialogState()
                                    flowerNum.value = flower.id
                                },
                                shape = CircleShape,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(top = 10.dp)
                                    .border(
                                        width = 3.dp,
                                        color = Color.LightGray,
                                        shape = CircleShape
                                    ),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                            ){
                                Image(painter = painterResource(id = flower.resource), contentDescription = "꽃")
                            }

                        }
                    }
                }
            }
        }
    }
}