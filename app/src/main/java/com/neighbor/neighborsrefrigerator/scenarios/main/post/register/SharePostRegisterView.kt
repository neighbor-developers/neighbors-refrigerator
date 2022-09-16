package com.neighbor.neighborsrefrigerator.scenarios.main.post.register

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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

private var sampleList: List<Pair<String, String>> = listOf(Pair("", "선택"), Pair("100", "과일"), Pair("101", "채소"))

@Composable
fun SharePostRegisterScreen(
    navHostController: NavHostController
) {
    val viewModel by remember {
        mutableStateOf(SharePostRegisterViewModel())
    }

    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.imgUriState = uri
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
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
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
            }
            if (completeBackDialog){
                CompleteDialog(
                    type = "취소",
                    { completeBackDialog = false},
                    { navHostController.navigateUp()})

            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "상품사진", modifier = Modifier.padding(start = 7.dp), fontSize = 13.sp, color = Color.DarkGray)
                Row() {
                    Image(
                        painter = if (viewModel.imgUriState != null)
                            rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = viewModel.imgUriState)
                                    .build()
                            ) else painterResource(R.drawable.camera),
                        contentDescription = "상품 사진",
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClickLabel = "Clickable image",
                                onClick = { selectImageLauncher.launch("image/*") }
                            )
                            .width(100.dp)
                            .height(100.dp)
                            .padding(top = 10.dp, end = 5.dp),
                    )

                    if (viewModel.imgUriState != null) {
                        viewModel.imgInputStream = LocalContext.current.contentResolver.openInputStream(
                            viewModel.imgUriState!!
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = 7.dp, end = 7.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = "상품명", fontSize = 13.sp, color = Color.DarkGray)
                    val cornerSize = CornerSize(10.dp)
                    TextField(
                        value = viewModel.title.value,
                        onValueChange = { input -> viewModel.title.value = input },
                        modifier = Modifier
                            .height(38.dp),
                        shape = MaterialTheme.shapes.large.copy(cornerSize),
                        placeholder = {
                            Text(
                                text = "상품명 입력",
                                style = TextStyle(
                                    fontSize = 11.5.sp,
                                    textDecoration = TextDecoration.None
                                ),
                                modifier = Modifier.padding(bottom = 0.dp)
                            )
                        },
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

                    Text(text = "기간", fontSize = 13.sp, color = Color.DarkGray)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Surface(modifier = Modifier.weight(1f)) {
                            PeriodButton("유통", viewModel.validateTypeName)
                        }
                        Surface(modifier = Modifier.weight(1f)) {
                            PeriodButton("제조", viewModel.validateTypeName)
                        }
                        Surface(modifier = Modifier.weight(1f)) {
                            PeriodButton("구매", viewModel.validateTypeName)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Black)
                                .clickable {
                                    periodButtonState = true
                                }
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = viewModel.validateDate.value,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(start = 10.dp, top = 3.dp, bottom = 3.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.calendar),
                                modifier = Modifier.width(15.dp).height(15.dp),
                                contentDescription = "calendar icon",
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

                            if (viewModel.validateImgUriState != null) {
                                viewModel.validateImgInputStream = LocalContext.current.contentResolver.openInputStream(
                                    viewModel.validateImgUriState!!
                                )
                            }
                        }
                        IconButton(
                            onClick = { selectValidateImageLauncher.launch("image/*") }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.picture_icon),
                                modifier = Modifier.width(20.dp).height(20.dp),
                                contentDescription = "picture icon",
                            )
                        }
                    }
                }
            }
            Text("카테고리", fontSize = 12.sp, color = Color.DarkGray)
            CategorySpinner(sampleList, Pair("", "선택"), viewModel)
            OutlinedTextField(
                value = viewModel.content.value,
                onValueChange = { input -> viewModel.content.value = input },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 10.dp)
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