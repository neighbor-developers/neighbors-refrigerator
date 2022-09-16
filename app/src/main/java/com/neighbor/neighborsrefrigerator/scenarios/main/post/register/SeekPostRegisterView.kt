package com.neighbor.neighborsrefrigerator.scenarios.main.post.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.viewmodels.SeekPostRegisterViewModel
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostRegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private var sampleList: List<Pair<String, String>> = listOf(Pair("", "선택"), Pair("100", "채소"), Pair("200", "과일"), Pair("300", "정육"), Pair("400", "수산"), Pair("500", "냉동"), Pair("600", "간편"))

@Composable
fun SeekPostRegisterScreen(
    navHostController: NavHostController,
) {
    val viewModel by remember {
        mutableStateOf(SeekPostRegisterViewModel())
    }

    var completeSeekDialog by remember {
        mutableStateOf(false)
    }
    // 뒤로가기 눌렀을때
    var completeBackDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("구함 등록", fontSize = 15.sp, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { completeBackDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_back),
                            tint = colorResource(id = R.color.green),
                            contentDescription = "뒤로가기 버튼",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        completeSeekDialog = true
                    })
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_register),
                            tint = colorResource(id = R.color.green),
                            contentDescription = "등록",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 25.dp, end = 25.dp, top = 10.dp, bottom = 20.dp)
        ) {
            if (completeSeekDialog) {
                CompleteDialog(
                    type = "구함",
                    { completeSeekDialog = false },
                    {
                        CoroutineScope(Dispatchers.Main).launch {
                            var postId = viewModel.registerPost()
                            if (postId != 0) {
                                navHostController.navigateUp()
                            }
                        }
                    }
                )

                if (viewModel.errorMessage.value.isNotEmpty()) {
                    Toast.makeText(LocalContext.current, viewModel.errorMessage.value, Toast.LENGTH_LONG).show()
                }
            }
            if (completeBackDialog) {
                CompleteDialog(
                    type = "취소",
                    { completeBackDialog = false },
                    { navHostController.navigateUp() })
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "제목", fontSize = 13.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 10.dp))
                val cornerSize = CornerSize(10.dp)
                TextField(
                    value = viewModel.title.value,
                    onValueChange = { input -> viewModel.title.value = input },
                    modifier = Modifier
                        .height(45.dp).padding(bottom = 10.dp),
                    shape = MaterialTheme.shapes.large.copy(cornerSize),
                    placeholder = { Text(text = "제목 입력", style = TextStyle(fontSize = 11.5.sp, textDecoration = TextDecoration.None), modifier = Modifier.padding(bottom = 0.dp)) },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkGray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.DarkGray,
                        backgroundColor = colorResource(id = R.color.backgroundGray),
                    ),
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        textDecoration = TextDecoration.None
                    ),
                )
                Text(text = "위치", fontSize = 13.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Surface(modifier = Modifier.weight(1f)) {
                        LocationButton("홈", viewModel.locationType)
                    }
                    Surface(modifier = Modifier.weight(1f)) {
                        LocationButton("내위치", viewModel.locationType)
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "카테고리",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    CategorySpinner(sampleList, Pair("", "선택"), viewModel)
                }
                OutlinedTextField(
                    value = viewModel.content.value,
                    onValueChange = { input -> viewModel.content.value = input },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = colorResource(id = R.color.green))
                )
            }
        }
    }
}

@Composable
private fun CategorySpinner(
    list: List<Pair<String, String>>,
    preselected: Pair<String, String>,
    viewModel: SeekPostRegisterViewModel
) {

    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    Box {
        Column {
            OutlinedTextField(
                value = (selected.second),
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, null) },
                readOnly = true
            )
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                list.forEach { entry ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            selected = entry
                            expanded = false

                            viewModel.category.value = selected.first
                        },
                        content = {
                            Text(
                                text = (entry.second),
                                modifier = Modifier.wrapContentWidth()
                            )
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { expanded = !expanded }
                )
        )
    }
}
