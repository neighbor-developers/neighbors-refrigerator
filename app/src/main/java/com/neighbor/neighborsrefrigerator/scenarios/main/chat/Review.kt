package com.neighbor.neighborsrefrigerator.scenarios.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.view.DeclarationDialog

@Composable
fun ReviewScreen(navController: NavHostController){
    var review by remember {
        mutableStateOf("")
    }
    var dialogState by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "후기 작성", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { dialogState = true }) {
                        Icon(Icons.Filled.Warning, contentDescription = "신고하기", tint = Color.Red)
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { padding ->
        Surface(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {

            if(dialogState) {
                DeclarationDialog(type = 2) { dialogState = false }
            }

            Column(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
                PostBox()

                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(text = "진키키", fontSize = 18.sp, color = Color.Blue)
                    Text(text = "님과의 거래가 만족스러우셨나요?", fontSize = 15.sp)
                }

                Text(text = "후기를 남겨주세요!", style = MaterialTheme.typography.h2, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(30.dp))

                RatingBar()
                OutlinedTextField(
                    value = review,
                    onValueChange = {review = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(top = 30.dp, bottom = 30.dp)
                )
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = { navController.navigate(NAV_ROUTE.MAIN.routeName) }
                    ) {
                        Text(text = "작성 완료")
                    }
                }
            }
        }

    }
}

@Composable
fun PostBox(){
    Card(elevation = 5.dp, modifier = Modifier
        .padding(top = 35.dp, bottom = 35.dp)) {
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth().padding(7.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.icon_google), contentDescription = "상품 사진", modifier = Modifier.size(50.dp))
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.height(50.dp).padding(start = 5.dp)) {
                Text(text = "감자", fontSize = 17.sp)
                Text(text = "어쩌구저쩌구", fontSize = 13.sp)
            }
        }

    }
}


@Composable
fun RatingBar(){
    var ratingState by remember{
        mutableStateOf(5)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)) {
        for (i in 1..5){
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "star",
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        ratingState = i
                    },
                tint = if(i <= ratingState) Color.Red else Color.LightGray
            )
        }
    }
}

@Composable
@Preview
fun ReviewPreview(){
    ReviewScreen(navController = rememberNavController())
}
