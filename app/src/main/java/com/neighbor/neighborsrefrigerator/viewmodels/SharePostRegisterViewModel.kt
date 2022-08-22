package com.neighbor.neighborsrefrigerator.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import java.text.SimpleDateFormat
import java.util.*

class SharePostRegisterViewModel: ViewModel() {
    private val dbAccessModule = DBAccessModule()
    private val userPrefs = UserSharedPreference(App.context())

    var locationType = mutableStateOf("")
    var validateTypeName = mutableStateOf("")
    var title = mutableStateOf(TextFieldValue())
    var content = mutableStateOf(TextFieldValue())
    var imgUriState by mutableStateOf<Uri?>(null)
    var validateDate = mutableStateOf("0000/00/00")
    var validateImgUriState by mutableStateOf<Uri?>(null)
    var category = mutableStateOf("")
    var isSuccessRegist = mutableStateOf(false)
    var myLatitude = mutableStateOf(0.0)
    var myLongitude = mutableStateOf(0.0)

    var errorMessage = mutableStateOf("")

    private var validateType = when (validateTypeName.value) {
        "제조" -> 1
        "구매" -> 2
        "유통" -> 3
        else -> 4
    }

    private fun myLocation(location: String): Array<Double> {
        return when (location) {
            "홈" -> arrayOf(
                userPrefs.getUserPrefs("latitude")!!.toDouble(),
                userPrefs.getUserPrefs("longitude")!!.toDouble()
            )
            "내위치" -> arrayOf(myLatitude.value, myLongitude.value)
            else -> arrayOf(0.0, 0.0)
        }
    }

    private fun validatePost(post: PostData) {
        if (post.title.isEmpty()) {
            errorMessage.value = "상품명을 입력해주세요"
        }
        else if (post.latitude.equals(0.0)) {
            errorMessage.value = "위치를 설정해주세요"
        }
        else if (post.categoryId.isEmpty()) {
            errorMessage.value = "카테고리를 설정해주세요"
        }
        else if (post.content.isEmpty()) {
            errorMessage.value = "내용을 입력해주세요"
        }
        else {
            errorMessage.value = ""
        }
    }

    fun registerData() {
        val location = myLocation(locationType.value)

        val postData = PostData(
            id = 1,
            title = title.value.text,
            categoryId = category.value,
            userId = userPrefs.getUserPrefs("id")!!.toInt(),
            content = content.value.text,
            rate = 0,
            review = "",
            createdAt = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(System.currentTimeMillis())),
            updatedAt = "",
            state = "1",
            validateType = validateType,
            validateDate = validateDate.value,
            validateImg = "https://newsimg-hams.hankookilbo.com/2022/04/28/a1844741-30af-475a-815e-226e2e319eae.jpg", // s3 개발 이후 추가
            productimg1 = "https://newsimg-hams.hankookilbo.com/2022/04/28/a1844741-30af-475a-815e-226e2e319eae.jpg", // s3 개발 이후 추가
            latitude = location[0],
            longitude = location[1],
            distance = 0.0,
            addrDetail = "",
            completedAt = "",
            mainAddr = "",
            type = 1,
        )

        validatePost(postData)

        if (errorMessage.value.isEmpty()) {
            dbAccessModule.entryPost(postData) {
                it.let {
                    isSuccessRegist.value = true
                }
            }
        }
    }
}
