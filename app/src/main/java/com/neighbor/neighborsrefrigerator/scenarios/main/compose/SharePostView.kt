package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighbor.neighborsrefrigerator.data.ProductData
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.scenarios.main.RouteAction
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SharePostScreen(
    sharePostViewModel: SharePostViewModel,
    route: NAV_ROUTE,
    routeAction: RouteAction
) {
Column {
        SearchBox(onSearch = { sharePostViewModel.search() })
        Text(
            text = "# 어쩌고님 위치에서 가까운 나눔",
            modifier = Modifier.padding(start = 30.dp, end = 15.dp, top = 30.dp, bottom = 10.dp),
            fontSize = 15.sp
        )
        SharePostListByDistance(productData = sharePostViewModel.products.collectAsState(), route, routeAction)
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp)
        ) {
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = 0f, y = canvasHeight),
                end = Offset(x = this.size.width, y = canvasHeight),
                color = Color.DarkGray,
                strokeWidth = 1F
            )
        }
        SharePostListByTime(productData = sharePostViewModel.products.collectAsState(), route, routeAction)
    }

}


@Composable
fun SharePostListByTime(productData: State<List<ProductIncludeDistanceData>?>, route: NAV_ROUTE, routeAction: RouteAction) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .padding(top = 10.dp, start = 30.dp, end = 30.dp),
        userScrollEnabled = true
    ) {
        productData.value?.let {
            items(productData.value!!) { item ->
                ItemCardByTime(item, route, routeAction)
            }
        }
    }
}

@Composable
fun SharePostListByDistance(productData: State<List<ProductIncludeDistanceData>?>, route: NAV_ROUTE, routeAction: RouteAction){
    Row (modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp))
    {
        productData.value?.let {
            it.forEach {
                ItemCardByDistance(product = it, route, routeAction)
            }
        }
    }
}

@Composable
fun SearchBox(onSearch: (String) -> Unit) {
    var content by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, end = 30.dp, start = 30.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        val cornersize = CornerSize(20.dp)
        OutlinedTextField(value = content, onValueChange = { content = it }, modifier = Modifier
            .fillMaxWidth()
            .size(45.dp), shape = MaterialTheme.shapes.large.copy(cornersize))

        IconButton(onClick = { onSearch(content) }) {
            Icon(Icons.Filled.Search, contentDescription = null)
        }
    }
}


@Preview
@Composable
fun Preview() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Text(text = "# 내 위치에서 가까운 나눔", modifier = Modifier.padding(start = 20.dp, end = 15.dp, top = 30.dp, bottom = 30.dp), fontSize = 18.sp)
        androidx.compose.foundation.Canvas(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)) {
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = 0f, y = canvasHeight),
                end = Offset(x = this.size.width, y = canvasHeight),
                color = Color.DarkGray,
                strokeWidth = 1F
            )
        }
    }
    var content by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 30.dp, start = 30.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        OutlinedTextField(value = content, onValueChange = { content = it }, modifier = Modifier
            .fillMaxWidth()
            .size(45.dp), shape = MaterialTheme.shapes.small)
        IconButton(onClick = { /*onSearch(content)*/ }) {
            Icon(Icons.Filled.Search, contentDescription = null)
        }
    }
}
