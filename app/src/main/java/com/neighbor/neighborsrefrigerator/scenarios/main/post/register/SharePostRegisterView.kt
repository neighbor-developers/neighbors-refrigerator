package com.neighbor.neighborsrefrigerator.scenarios.main.post.register

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostRegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

private var sampleList: List<Pair<String, String>> = listOf(Pair("", "선택"), Pair("100", "채소"), Pair("200", "과일"), Pair("300", "정육"), Pair("400", "수산"), Pair("500", "냉동"), Pair("600", "간편"))

@Composable
fun SharePostRegisterScreen(
    navHostController: NavHostController
) {
    val viewModel by remember {
        mutableStateOf(SharePostRegisterViewModel())
    }

    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.imgUriState1 = uri
    }
    val selectImage2Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.imgUriState2 = uri
    }
    val selectImage3Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.imgUriState3 = uri
    }
    val selectValidateImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.validateImgUriState = uri
    }

    var periodButtonState by remember { mutableStateOf(false) }

    val mCalendar = Calendar.getInstance()

    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    var completeShareDialog by remember {
        mutableStateOf(false)
    }
    // 뒤로가기 눌렀을때
    var completeBackDialog by remember {
        mutableStateOf(false)
    }

    mCalendar.time = Date()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("나눔 등록", fontSize = 15.sp, textAlign = TextAlign.Center) },
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
                        completeShareDialog = true
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
            if(completeShareDialog){
                CompleteDialog(
                    type = "등록",
                    { completeShareDialog = false },
                    {
                        CoroutineScope(Dispatchers.Main).launch {
                            var postId = viewModel.registerPost()
                            if (postId != 0) {
                                navHostController.navigateUp()
                            } else {
                                Log.d("실패", "상품 등록 실패")
                            }
                        }
                    }
                )

                if (viewModel.errorMessage.value.isNotEmpty()) {
                    Toast.makeText(LocalContext.current, viewModel.errorMessage.value, Toast.LENGTH_LONG).show()
                }
            }
            if (completeBackDialog){
                CompleteDialog(
                    type = "취소",
                    { completeBackDialog = false},
                    { navHostController.navigateUp()})
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "상품사진", modifier = Modifier.padding(bottom = 10.dp), fontSize = 13.sp, color = Color.DarkGray)
                Row(verticalAlignment = Alignment.Bottom) {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.imgUriState1 == null) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRoundRect(
                                    color = Color.Black,
                                    style = Stroke(width = 2f,
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                    )
                                )
                            }
                        }
                        Image(
                            painter = if (viewModel.imgUriState1 != null)
                                rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = viewModel.imgUriState1)
                                        .build()
                                ) else painterResource(R.drawable.ic_baseline_add_24),
                            contentDescription = "상품 사진",
                            modifier = Modifier
                                .clickable(
                                    enabled = true,
                                    onClickLabel = "Clickable image",
                                    onClick = { selectImageLauncher.launch("image/*") }
                                )
                                .size(if (viewModel.imgUriState1 != null) 100.dp else 60.dp)
                                .aspectRatio(1f)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (viewModel.imgUriState1 != null) {
                        viewModel.imgInputStream1 = LocalContext.current.contentResolver.openInputStream(
                            viewModel.imgUriState1!!
                        )

                        Box(
                            modifier = Modifier
                                .size(100.dp, 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (viewModel.imgUriState2 == null) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawRoundRect(
                                        color = Color.Black,
                                        style = Stroke(width = 2f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                        )
                                    )
                                }
                            }
                            Image(
                                painter = if (viewModel.imgUriState2 != null)
                                    rememberAsyncImagePainter(
                                        ImageRequest
                                            .Builder(LocalContext.current)
                                            .data(data = viewModel.imgUriState2)
                                            .build()
                                    ) else painterResource(R.drawable.ic_baseline_add_24),
                                contentDescription = "상품 사진",
                                modifier = Modifier
                                    .clickable(
                                        enabled = true,
                                        onClickLabel = "Clickable image",
                                        onClick = { selectImage2Launcher.launch("image/*") }
                                    )
                                    .size(if (viewModel.imgUriState2 != null) 100.dp else 60.dp)
                                    .aspectRatio(1f)
                                    .padding(end = 5.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (viewModel.imgUriState2 != null) {
                            viewModel.imgInputStream2 =
                                LocalContext.current.contentResolver.openInputStream(
                                    viewModel.imgUriState2!!
                                )

                            Box(
                                modifier = Modifier
                                    .size(100.dp, 100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (viewModel.imgUriState3 == null) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawRoundRect(
                                            color = Color.Black,
                                            style = Stroke(
                                                width = 2f,
                                                pathEffect = PathEffect.dashPathEffect(
                                                    floatArrayOf(
                                                        10f,
                                                        10f
                                                    ), 0f
                                                )
                                            )
                                        )
                                    }
                                }
                                Image(
                                    painter = if (viewModel.imgUriState3 != null)
                                        rememberAsyncImagePainter(
                                            ImageRequest
                                                .Builder(LocalContext.current)
                                                .data(data = viewModel.imgUriState3)
                                                .build()
                                        ) else painterResource(R.drawable.ic_baseline_add_24),
                                    contentDescription = "상품 사진",
                                    modifier = Modifier
                                        .clickable(
                                            enabled = true,
                                            onClickLabel = "Clickable image",
                                            onClick = { selectImage3Launcher.launch("image/*") }
                                        )
                                        .size(if (viewModel.imgUriState3 != null) 100.dp else 60.dp)
                                        .aspectRatio(1f)
                                        .padding(end = 5.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            if (viewModel.imgUriState3 != null) {
                                viewModel.imgInputStream3 =
                                    LocalContext.current.contentResolver.openInputStream(
                                        viewModel.imgUriState3!!
                                    )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(end = 7.dp, top = 7.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = "상품명", fontSize = 13.sp, color = Color.DarkGray)
                    val cornerSize = CornerSize(10.dp)
                    TextField(
                        value = viewModel.title.value,
                        onValueChange = { input -> viewModel.title.value = input },
                        modifier = Modifier
                            .height(45.dp),
                        shape = MaterialTheme.shapes.large.copy(cornerSize),
                        placeholder = { Text(text = "상품명 입력", style = TextStyle(fontSize = 11.5.sp, textDecoration = TextDecoration.None), modifier = Modifier.padding(bottom = 0.dp)) },
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

                    Text(text = "위치", fontSize = 13.sp, color = Color.DarkGray)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Surface(modifier = Modifier.weight(1f)) {
                            LocationButton("홈", viewModel.locationType)
                        }
                        Surface(modifier = Modifier.weight(1f)) {
                            LocationButton("내위치", viewModel.locationType)
                        }
                    }

                    Text(text = "유통기한", fontSize = 13.sp, color = Color.DarkGray)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.weight(1f)) {
                            PeriodButton("유통", viewModel.validateTypeName)
                        }
                        Surface(modifier = Modifier.weight(1f)) {
                            PeriodButton("제조", viewModel.validateTypeName)
                        }
                        Surface(modifier = Modifier.weight(1f)) {
                            PeriodButton("구매", viewModel.validateTypeName)
                        }
                        Surface(modifier = Modifier
                            .weight(2f)
                            .padding(start = 10.dp)) {
                            Row(modifier = Modifier.clickable {
                                periodButtonState = true
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.calendar),
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = "calendar icon",
                                )
                                Text(
                                    text = viewModel.validateDate.value,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 15.dp),
                                    fontSize = 13.sp,
                                )
                                if (periodButtonState) {
                                    DatePickerDialog(
                                        LocalContext.current,
                                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                            viewModel.validateDate.value =
                                                "$mYear/${mMonth + 1}/$mDayOfMonth"
                                        }, mYear, mMonth, mDay
                                    ).show()
                                    periodButtonState = false
                                }
                            }
                        }
                    }

                    if (viewModel.validateDate.value != "") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { selectValidateImageLauncher.launch("image/*") }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.picture_icon),
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp),
                                    contentDescription = "picture icon",
                                )
                            }

                            if (viewModel.validateImgUriState != null) {
                                viewModel.validateImgInputStream = LocalContext.current.contentResolver.openInputStream(
                                    viewModel.validateImgUriState!!
                                )
                            }

                            Text(text = if (viewModel.validateImgUriState != null) "사진이 등록되었습니다" else "유통기한 인증 사진을 올려주세요",
                                color = Color.Gray,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )
                        }
                    }
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


@Composable
fun LocationButton(name: String, locationType: MutableState<String>) {

    Button(
        content = {
            Text(text = name, fontSize = 12.sp, fontWeight = if( locationType.value == name ) FontWeight.Bold else FontWeight.Normal)
        },
        onClick = { locationType.value = name },
        colors = if( locationType.value == name )
            ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.green),
                contentColor = Color.White)
        else ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black),
        modifier = Modifier.height(30.dp)
    )

}

@Composable
fun PeriodButton(name: String, periodType: MutableState<String>) {
    Button(
        content = {
            Text(text = name, fontSize = 12.sp, fontWeight = if( periodType.value == name ) FontWeight.Bold else FontWeight.Normal)
        },
        onClick = { periodType.value = name },
        colors = if(periodType.value == name)  ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.green),
            contentColor = Color.White)
        else ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black),
        modifier = Modifier.height(30.dp)

    )
}

@Composable
private fun CategorySpinner(
    list: List<Pair<String, String>>,
    preselected: Pair<String, String>,
    viewModel: SharePostRegisterViewModel
) {

    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    Box {
        Column {
            OutlinedTextField(
                value = (selected.second),
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, null) },
                readOnly = true,
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
fun registerPreview() {
    val navController = rememberNavController()
    SharePostRegisterScreen(
        navHostController = navController
    )
}