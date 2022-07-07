package com.neighbor.neighborsrefrigerator.scenarios.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.SharePostDetailsScreen
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostDetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(text = "이웃집 냉장고")
//                },
//                actions = {
//                    IconButton(onClick = {  }) {
//                        Icon(Icons.Filled.Notifications, contentDescription = "")
//                    }
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(Icons.Filled.Menu, contentDescription = "")
//                    }
//                }
//            ) {
//            }
//        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }, contentColor = Color.Black, backgroundColor = Color.Yellow) {
                Icon(Icons.Filled.Edit, contentDescription = "a")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true
    ) {
        // 안에 페이지 넣으면 댐 - 나눔페이지
        SharePostDetailsScreen(
            sharePostDetailViewModel = SharePostDetailViewModel(),
            onBackClick = { /*TODO*/ },
            onAccuseClick = { /*TODO*/ }
        ) {
//
        }
        Text(text = "A", modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MainScreen()
}
