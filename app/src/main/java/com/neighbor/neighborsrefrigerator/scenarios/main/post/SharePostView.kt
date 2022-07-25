package com.neighbor.neighborsrefrigerator.scenarios.main.post

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.ItemCardByDistance
import com.neighbor.neighborsrefrigerator.scenarios.main.compose.ItemCardByTime
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostViewModel

@Composable
fun SharePostScreen(
    sharePostViewModel: SharePostViewModel,
    route: NAV_ROUTE,
    navHostController: NavHostController
) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(0) }
    Column(
        modifier = Modifier.verticalScroll(state).height(1000.dp)
    ) {
        SearchBox(sharePostViewModel, "share")
        SharePostListByDistance(productData = sharePostViewModel.products.collectAsState(), route, navHostController)
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
        SharePostListByTime(productData = sharePostViewModel.products.collectAsState(), route, navHostController, state)
    }

}


@Composable
fun SharePostListByTime(productData: State<List<ProductIncludeDistanceData>?>, route: NAV_ROUTE, navHostController: NavHostController, state: ScrollState) {

    val scrollState = rememberLazyGridState()

    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier
            .padding(top = 10.dp, start = 30.dp, end = 30.dp),
        userScrollEnabled = true
    ) {
        productData.value?.let {
            items(productData.value!!) { item ->
                ItemCardByTime(item, route, navHostController)
            }
        }
    }


}

@Composable
fun SharePostListByDistance(productData: State<List<ProductIncludeDistanceData>?>, route: NAV_ROUTE, navHostController: NavHostController){
    Text(
        text = "# 어쩌고님 위치에서 가까운 나눔",
        modifier = Modifier.padding(start = 30.dp, end = 15.dp, top = 30.dp, bottom = 10.dp),
        fontSize = 15.sp
    )
    Row (modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp))
    {
        productData.value?.let {
            it.forEach {
                ItemCardByDistance(product = it, route, navHostController)
            }
        }
    }
}

@Composable
fun SearchBox(sharePostViewModel: SharePostViewModel, type: String) {
    var content by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, end = 30.dp, start = 30.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        val cornerSize = CornerSize(20.dp)
        OutlinedTextField(value = content, onValueChange = { content = it }, modifier = Modifier
            .fillMaxWidth()
            .size(45.dp), shape = MaterialTheme.shapes.large.copy(cornerSize))

        IconButton(onClick = {sharePostViewModel.search(content, type)}) {
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
        Canvas(modifier = Modifier
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
