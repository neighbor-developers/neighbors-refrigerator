package com.neighbor.neighborsrefrigerator.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64.NO_WRAP
import android.util.Base64.encodeToString
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.async
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class SharePostRegisterViewModel: ViewModel() {
    private val dbAccessModule = DBAccessModule()
    private val userPrefs = UserSharedPreference(App.context())

    var locationType = mutableStateOf("홈")
    var validateTypeName = mutableStateOf("유통")
    var title = mutableStateOf(TextFieldValue())
    var content = mutableStateOf(TextFieldValue())
    var imgUriState1 by mutableStateOf<Uri?>(null)
    var imgUriState2 by mutableStateOf<Uri?>(null)
    var imgUriState3 by mutableStateOf<Uri?>(null)
    var imgInputStream by mutableStateOf<InputStream?>(null)
    var validateDate = mutableStateOf("")
    var validateImgUriState by mutableStateOf<Uri?>(null)
    var validateImgInputStream by mutableStateOf<InputStream?>(null)
    var category = mutableStateOf("")
    var myLatitude = mutableStateOf(0.0)
    var myLongitude = mutableStateOf(0.0)

    private fun encodeImgToBase64(ins: InputStream): String {
        // Base64 인코딩부분
        val img: Bitmap = BitmapFactory.decodeStream(ins)
        val resized = Bitmap.createScaledBitmap(img, 256, 256, true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        return encodeToString(byteArray, NO_WRAP)
    }

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
            validateType = validateType,
            validateDate = validateDate.value,
            validateImg = validateImgInputStream?.let { encodeImgToBase64(it) },
            productimg1 = imgInputStream?.let { encodeImgToBase64(it) },
            productimg2 = "",
            productimg3 = "",
            latitude = location[0],
            longitude = location[1],
            distance = 0.0,
            addrDetail = "",
            completedAt = "",
            mainAddr = "",
            type = 1,
        )

        Log.d("product", postData.toString())

        validatePost(postData)

        var postId = 0

        viewModelScope.async {
            postId = dbAccessModule.entryPost(postData)
        }.await()

        return postId
    }
}
