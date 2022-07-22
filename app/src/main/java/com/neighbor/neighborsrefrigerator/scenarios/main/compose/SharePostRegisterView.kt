package com.neighbor.neighborsrefrigerator.scenarios.main.compose

import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.scenarios.main.NAV_ROUTE
import com.neighbor.neighborsrefrigerator.viewmodels.SharePostRegisterViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class SharePostRegisterCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: () -> Unit
)

var sampleList: List<Pair<String, String>> = listOf(Pair("100", "과일"), Pair("101", "채소"))

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharePostRegisterScreen(
    viewModel: SharePostRegisterViewModel,
    navHostController: NavHostController,
    route: NAV_ROUTE,
) {
    var locationType = remember { mutableStateOf("") }
    var periodType = remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }


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
                    IconButton(onClick = { /* doSomething() */ }) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_google),
                    contentDescription = "상품 사진",
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .clickable(
                            enabled = true,
                            onClickLabel = "Clickable image",
                            onClick = { }
                        )
                )
                Column() {
                    Row(modifier = Modifier.padding(bottom = 5.dp)) {

                        Text(text = "상품명", fontSize = 20.sp)
                        TextField(
                            value = title,
                            onValueChange = { title },
                            modifier = Modifier
                                .height(20.dp)
                                .padding(start = 5.dp),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                    Row(modifier = Modifier.padding(bottom = 5.dp)) {
                        Text(text = "위치", fontSize = 20.sp, modifier = Modifier.padding(end = 5.dp))
                        LocationButton("홈", locationType)
                        LocationButton("내위치", locationType)
                    }
                    Row(modifier = Modifier.padding(bottom = 5.dp)) {
                        Text(text = "기간", fontSize = 20.sp, modifier = Modifier.padding(end = 5.dp))
                        PeriodButton("제조", periodType)
                        PeriodButton("구매", periodType)
                        PeriodButton("유통", periodType)
                    }
                    Row(
                        modifier = Modifier.border(width = 1.dp, color = Color.Black).clickable {
                            /* DatePicker(onDateSelected = { }, onDismissRequest = {}) */
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "2022.07.30", fontSize = 12.sp)
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기 버튼"
                        )
                    }
                }
            }
            Column() {
                Row() {
                    Text("카테고리")
                    SampleSpinner(sampleList, Pair("100", "과일"))
                }
                Box() {
                    TextField(
                        value = content,
                        onValueChange = { content },
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "채팅"
                        )
                    }
                }
            }
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
        modifier = Modifier.height(30.dp).width(90.dp).padding(end = 5.dp)
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
        modifier = Modifier.height(30.dp).width(60.dp).padding(end = 5.dp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select date",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    // TODO - hardcode string
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    // TODO - hardcode string
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            view.minDate = 30 // contraints
            view.maxDate = 60 // contraints

            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate
                        .now()
                        .withMonth(month + 1)
                        .withYear(year)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}

@Composable
fun SampleSpinner(
    list: List<Pair<String, String>>,
    preselected: Pair<String, String>,
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DefaultPreview() {
    SharePostRegisterScreen(
        viewModel = SharePostRegisterViewModel(),
        route = NAV_ROUTE.WRITE_SHARE,
        navHostController = NavHostController(context = LocalContext.current)
    )
}
