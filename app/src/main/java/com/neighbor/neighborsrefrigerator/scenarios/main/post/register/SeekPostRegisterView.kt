package com.neighbor.neighborsrefrigerator.scenarios.main.post.register

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.viewmodels.SeekPostRegisterViewModel

private var sampleList: List<Pair<String, String>> = listOf(Pair("100", "과일"), Pair("101", "채소"))

@Composable
fun SeekPostRegisterScreen(
    navHostController: NavHostController,
    viewModel: SeekPostRegisterViewModel = viewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("구함 등록") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기 버튼"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.registerData()

                        if (viewModel.isSuccessRegist.value) {
                            navHostController.navigateUp()
                        } else {
                            Log.d("실패", "상품 등록 실패")
                        }
                    })
                    {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "등록"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 10.dp, end = 10.dp, top = 30.dp, bottom = 10.dp)
        ) {
            Row(modifier = Modifier.padding(bottom = 5.dp)) {
                Text(text = "제목", fontSize = 20.sp)
                TextField(
                    value = viewModel.title.value,
                    onValueChange = { input -> viewModel.title.value = input },
                    modifier = Modifier
                        .padding(start = 5.dp),
                )
            }
            Row(modifier = Modifier.padding(bottom = 5.dp)) {
                Text(text = "위치", fontSize = 20.sp, modifier = Modifier.padding(end = 5.dp))
                LocationButton("홈", viewModel.locationType)
                LocationButton("내위치", viewModel.locationType)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("카테고리")
                CategorySpinner(sampleList, Pair("100", "과일"), viewModel)
            }
            TextField(
                value = viewModel.content.value,
                onValueChange = { input -> viewModel.content.value = input },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
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

@Preview
@Composable
fun SeekPreview() {
    SeekPostRegisterScreen(
        navHostController = NavHostController(context = LocalContext.current)
    )
}