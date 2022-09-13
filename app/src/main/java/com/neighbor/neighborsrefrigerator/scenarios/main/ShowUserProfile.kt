package com.neighbor.neighborsrefrigerator.scenarios.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.ui.theme.NeighborsRefrigeratorTheme

@Composable
fun ShowUserProfile() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(30.dp, 20.dp, 15.dp, 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.level4_ver3),
                    contentDescription = "profileImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.LightGray, CircleShape)
                )

                Text(
                    text = "정왕동 땅부자",
                    style = MaterialTheme.typography.overline,
                    color = Color.DarkGray,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(15.dp, 35.dp, 0.dp, 0.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(30.dp, 20.dp, 15.dp, 20.dp)
            ) {
                Text(
                    text = "거래 점수",
                    style = MaterialTheme.typography.overline,
                    color = Color.DarkGray,
                    fontSize = 25.sp,
                )
                RatingBar(rating = 3)
            }

            Text(
                text = "거래 횟수 36회",
                style = MaterialTheme.typography.overline,
                color = Color.DarkGray,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(30.dp, 0.dp, 0.dp, 20.dp)
            )
            displayList()
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int
) {
    var ratingState by remember { mutableStateOf(rating) }

    Row(
        modifier = modifier
            .padding(30.dp, 0.dp, 0.dp, 0.dp)
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(33.dp)
                    .height(33.dp)
                    .clickable {
                        ratingState = i
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
        Text(
            text = "3.7점",
            style = MaterialTheme.typography.overline,
            color = Color.LightGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .padding(5.dp, 9.dp, 0.dp, 0.dp)
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun displayList() {
    val languages = listOf(
        "C++", "C", "C#", "Java", "Kotlin", "Dart", "Python", "Javascript", "SpringBoot",
        "XML", "Dart", "Node JS", "Typescript", "Dot Net", "GoLang", "MongoDb",
    )
    Column(
        modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, Color.LightGray))
            .fillMaxHeight()
            .padding(30.dp, 8.dp, 30.dp, 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(languages) { language ->
                Text(language,
                    color = Color.DarkGray,
                    fontSize = 25.sp
                )
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NeighborsRefrigeratorTheme {
        ShowUserProfile()
    }
}