package com.neighbor.neighborsrefrigerator.scenarios.main.post.register

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.view.CompleteDialog
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostRegisterViewModel
import java.util.*

private var sampleList: List<Pair<String, String>> = listOf(Pair("", "선택"), Pair("100", "과일"), Pair("101", "채소"))

@Composable
fun SharePostRegisterScreen(
    navHostController: NavHostController,
    viewModel: SharePostRegisterViewModel = viewModel()
) {

    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.imgUriState = uri
    }
    val selectValidateImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.validateImgUriState = uri
    }

    var periodButtonState by remember { mutableStateOf(false) }

    val mCalendar = Calendar.getInstance()

    var mYear = mCalendar.get(Calendar.YEAR)
    var mMonth = mCalendar.get(Calendar.MONTH)
    var mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    var completeShareDialog by remember {
        mutableStateOf(false)
    }

    mCalendar.time = Date()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("나눔 등록") },
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
                        completeShareDialog = true
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
            if(completeShareDialog){
                CompleteDialog(
                    type = "등록",
                    { completeShareDialog = false },
                    {
                        if (viewModel.isSuccessRegist.value) {
                            navHostController.navigateUp()
                        } else {
                            Log.d("실패", "상품 등록 실패")
                        }
                    }
                )
            }

            Row() {
                Image(
                    painter = if (viewModel.imgUriState != null)
                        rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = viewModel.imgUriState)
                                .build()
                        ) else painterResource(R.drawable.icon_google),
                    contentDescription = "상품 사진",
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClickLabel = "Clickable image",
                            onClick = { selectImageLauncher.launch("image/*") }
                        )
                        .width(150.dp)
                        .height(150.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(start = 10.dp)
                ) {
                    Row(modifier = Modifier.padding(bottom = 5.dp)) {
                        Text(text = "상품명", fontSize = 20.sp)
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
                    Row(
                        modifier = Modifier.padding(bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "기간", fontSize = 20.sp, modifier = Modifier.padding(end = 5.dp))
                        Column() {
                            Row(modifier = Modifier.padding(bottom = 5.dp)) {
                                PeriodButton("제조", viewModel.validateTypeName)
                                PeriodButton("구매", viewModel.validateTypeName)
                                PeriodButton("유통", viewModel.validateTypeName)
                            }
                            Row() {
                                Row(
                                    modifier = Modifier
                                        .border(width = 1.dp, color = Color.Black)
                                        .clickable {
                                            periodButtonState = true
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = viewModel.validateDate.value,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .width(120.dp)
                                            .padding(start = 10.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "달력 버튼"
                                    )

                                    if (periodButtonState) {
                                        DatePickerDialog(
                                            LocalContext.current,
                                            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                                viewModel.validateDate.value = "$mYear/${mMonth+1}/$mDayOfMonth"
                                            }, mYear, mMonth, mDay
                                        ).show()
                                        periodButtonState = false
                                    }
                                }
                                IconButton(
                                    onClick = { selectValidateImageLauncher.launch("image/*") }
                                ){
                                    Icon(
                                        Icons.Filled.Refresh,
                                        contentDescription = "Localized description",
                                        tint = Color.Blue
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("카테고리")
                CategorySpinner(sampleList, Pair("", "선택"), viewModel)
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
fun LocationButton(name: String, locationType: MutableState<String>) {
    OutlinedButton(
        content = {
            Text(text = name, fontSize = 12.sp)
        },
        onClick = { locationType.value = name },
        colors = ChangeButtonColor(locationType.value == name),
        modifier = Modifier
            .height(30.dp)
            .width(90.dp)
            .padding(end = 5.dp)
    )
}

@Composable
fun PeriodButton(name: String, periodType: MutableState<String>) {
    OutlinedButton(
        content = {
            Text(text = name, fontSize = 10.sp)
        },
        onClick = { periodType.value = name },
        colors = ChangeButtonColor(periodType.value == name),
        modifier = Modifier
            .height(30.dp)
            .width(60.dp)
            .padding(end = 5.dp)
    )
}

@Composable
fun ChangeButtonColor(isSelect: Boolean): ButtonColors {
    return if (isSelect) {
        ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)
    } else {
        ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Blue)
    }
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
fun SharePreview() {
    SharePostRegisterScreen(
        navHostController = NavHostController(context = LocalContext.current)
    )
}
