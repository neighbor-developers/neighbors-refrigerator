package com.neighbor.neighborsrefrigerator.scenarios.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.neighbor.neighborsrefrigerator.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GuideScreen() {
    val slideImage = remember { mutableStateOf(R.drawable.category_200) }
    val pagerState = rememberPagerState(pageCount = 3)

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when(page)  {
            0 -> {
                slideImage.value = R.drawable.category_300
            }
            1 -> {
                slideImage.value = R.drawable.category_400
            }
            2 -> {
                slideImage.value = R.drawable.category_500
            }
        }

        Image(
            painterResource(slideImage.value),
            modifier = Modifier.fillMaxWidth(),
            contentDescription = ""
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier.fillMaxWidth()) {
        DotsIndicator(
            totalDots = 3,
            selectedIndex = pagerState.currentPage,
            selectedColor = Color.Blue,
            unSelectedColor = Color.LightGray
        )

        if (pagerState.currentPage == 2) {
            Button(
                content = {
                    Text(text = "시작하기", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                },
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.green),
                        contentColor = Color.White),
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots : Int,
    selectedIndex : Int,
    selectedColor: Color,
    unSelectedColor: Color,
){
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()

    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Preview(name = "Long greeting")
@Composable
fun PreviewGuideScreen() {
    GuideScreen()
}