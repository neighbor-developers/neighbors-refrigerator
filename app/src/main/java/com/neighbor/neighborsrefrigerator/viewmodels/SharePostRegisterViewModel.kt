package com.neighbor.neighborsrefrigerator.viewmodels

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.PostData
import java.lang.System.currentTimeMillis
import java.util.*

class SharePostRegisterViewModel: ViewModel() {
    var locationType = mutableStateOf("")
    var validateTypeName = mutableStateOf("")
    var title by mutableStateOf(TextFieldValue())
    var content by mutableStateOf(TextFieldValue())
    var imgUriState by mutableStateOf<Uri?>(null)
    var validateDate by mutableStateOf("0000.00.00")
    var category by mutableStateOf("")

    private var validateType = when (validateTypeName.toString()) {
        "제조" -> 1
        "구매" -> 2
        "유통" -> 3
        else -> 4
    }

    fun registerData() {
//        val postData = PostData(
//            id = 1,
//            title = title.text,
//            categoryId = category,
//            userId = 1,
//            content = content.text,
//            postType = 1,
//            postAddr = "",
//            rate = 0.0,
//            review = "",
//            createdAt = Date(currentTimeMillis()),
//            updatedAt = Date(currentTimeMillis()),
//            state = "new",
//            validateType = validateType,
//            validateDate = Date(currentTimeMillis()),
//            validateImg = "",
//            productImg = ""
//        )
    }
}
