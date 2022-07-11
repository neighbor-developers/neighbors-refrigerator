package com.neighbor.neighborsrefrigerator.scenarios.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SeekPostScreen
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SharePostScreen
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        sheetBackgroundColor = Color.LightGray,
        sheetContentColor = Color.Black,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Category")
            }
            /*카테고리 내용*/
            Column(Modifier.padding(20.dp)) {
                Text(text = "어쩌구~")
                Text(text = "어쩌구~")
                Text(text = "어쩌구~")
                Text(text = "어쩌구~")
                Text(text = "어쩌구~")
            }
             //categoryView(categoryList = )
        },
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                },
                actions = {
                    IconButton(onClick = { /* 채팅 페이지 이동*/ }) {
                        Icon(Icons.Filled.Send, contentDescription = "")
                    }
                    IconButton(onClick = { /* 마이페이지 드로어 */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.background
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isDropDownMenuExpanded = true }, contentColor = Color.Black, backgroundColor = Color.Yellow) {
                Icon(Icons.Filled.Edit, contentDescription = "a")
                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false },
                    offset = DpOffset((10).dp, 0.dp)
                ) {
                    DropdownMenuItem(onClick = { /*나눔 글쓰기 페이지 이동*/ }) {
                        Text("나눔")
                    }
                    DropdownMenuItem(onClick = { /*구함 글쓰기 페이지 이동*/ }) {
                        Text("구함")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 40.dp
    ) {
        Column(modifier = Modifier.padding(it).fillMaxSize()) {
            Row(){
                TextButton(onClick = { /*나눔페이지*/}, modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)) {
                    Text(text = "나눔")
                }
                TextButton(onClick = { /*구함페이지*/ }, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                    Text(text = "구함")
                }
            }
            SharePostScreen(
                sharePostViewModel = SharePostViewModel()
            )

        }
    }
}

// @Composable
// fun categoryView(categoryList: List<String>){
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(4),
//        verticalArrangement = Arrangement.spacedBy(20.dp),
//        horizontalArrangement = Arrangement.spacedBy(20.dp),
//        modifier = Modifier.padding(30.dp)
//    ) {
//        items(categoryList){ item ->
//            CategoryItemView(item)
//        }
//
//    }
// }

// @OptIn(ExperimentalMaterialApi::class)
// @Composable
// fun CategoryItemView(item: String){
//    Card(onClick = categoryView(categoryList = )) {
//        Icon(Icons.Filled.Favorite, contentDescription = item)
//    }
// }

@Preview
@Composable
fun DefaultPreview() {
    MainScreen()
}
