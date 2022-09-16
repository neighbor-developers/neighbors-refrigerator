package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.util.*

class SeekPostRegisterViewModel: ViewModel() {
    private val dbAccessModule = DBAccessModule()
    private val userPrefs = UserSharedPreference(App.context())

    var locationType = mutableStateOf("")
    var title = mutableStateOf(TextFieldValue())
    var content = mutableStateOf(TextFieldValue())
    var category = mutableStateOf("")
    var myLatitude = mutableStateOf(0.0)
    var myLongitude = mutableStateOf(0.0)
    var errorMessage = mutableStateOf("")

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

    suspend fun registerPost(): Int {
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
            validateType = 0,
            validateDate = "",
            validateImg = "", // s3 개발 이후 추가
            productimg1 = "", // s3 개발 이후 추가
            productimg2 = "",
            productimg3 = "",
            latitude = location[0],
            longitude = location[1],
            distance = 0.0,
            addrDetail = "",
            completedAt = "",
            mainAddr = "",
            type = 2,
        )
        Log.d("구함 등록 데이터", postData.toString())

        validatePost(postData)

        var postId = 0

        viewModelScope.async {
            postId = dbAccessModule.entryPost(postData)
        }.await()

        return postId
    }
}
