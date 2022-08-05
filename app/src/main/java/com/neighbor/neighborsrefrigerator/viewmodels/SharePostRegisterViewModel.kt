package com.neighbor.neighborsrefrigerator.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.network.DBAccessModule

class SharePostRegisterViewModel: ViewModel() {
    private val dbAccessModule = DBAccessModule()

    var locationType = mutableStateOf("")
    var validateTypeName = mutableStateOf("")
    var title = mutableStateOf(TextFieldValue())
    var content = mutableStateOf(TextFieldValue())
    var imgUriState by mutableStateOf<Uri?>(null)
    var validateDate = mutableStateOf("0000/00/00")
    var validateImgUriState by mutableStateOf<Uri?>(null)
    var category = mutableStateOf("")
    var isSuccessRegist = mutableStateOf(false)

    private var validateType = when (validateTypeName.toString()) {
        "제조" -> 1
        "구매" -> 2
        "유통" -> 3
        else -> 4
    }

    fun registerData() {
        val postData = PostData(
            id = 1,
            title = title.value.text,
            categoryId = category.value,
            userId = 1,
            content = content.value.text,
            rate = 0,
            review = "",
            createdAt = "",
            updatedAt = "",
            state = "1",
            validateType = validateType,
            validateDate = validateDate.value,
            validateImg = "",
            productImg = "",
            latitude = 0.0,
            longitude = 0.0,
            distance = 0.0,
            addrDetail = "",
            completedAt = "",
            mainAddr = "",
            type = 1,
        )

        dbAccessModule.entryPost(postData) {
            if (it == 200)
                isSuccessRegist.value = true
        }
    }
}
