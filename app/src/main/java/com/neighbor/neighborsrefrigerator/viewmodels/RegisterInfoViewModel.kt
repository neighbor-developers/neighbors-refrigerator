package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.UseGeocoder
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*

// textfield에 들어와 있는 데이터 저장
// nickname의 경우 변화가 감지될 경우 뷰모델에서 사용할 수 있는지 확인 후 true false 반환해 아이콘 색 변경해주기
// address의 경우 dialog에서 선택한 address를 클릭 시 textfield의 address에 자동으로 넣기
// dialog에서 address 검색 구성하기

class RegisterInfoViewModel: ViewModel() {
    val dbAccessModule = DBAccessModule()

    var userNicknameInput by mutableStateOf("")
    var addressMain by mutableStateOf("")
    var addressDetail by mutableStateOf("")
    val availableNickname = MutableStateFlow(false)
    val fillAddressMain = MutableStateFlow(false)

    var buttonEnabled = MutableStateFlow(false)

    fun check(){
        buttonEnabled.value = availableNickname.value
    }

    val time = System.currentTimeMillis()
    private val timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(time))

    fun checkNickname() {
        if (userNicknameInput.isEmpty()) {
            availableNickname.value = false
            return
        }
        dbAccessModule.checkNickname(userNicknameInput) {completeCheck()}


    }

    private fun completeCheck() {
        availableNickname.value = true
        fillAddressMain.value = addressMain.isNotEmpty()
        buttonEnabled.value = availableNickname.value && fillAddressMain.value
    }

    fun registerPersonDB() {

        val coordinateData: LatLng = UseGeocoder().addressToLatLng(addressMain + addressDetail)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val uID: String = if (auth.uid != null) {
            auth.uid!!
        } else {
            ""
        }
        val email: String? = if (auth.currentUser?.email != null) {
            auth.currentUser!!.email
        } else {
            ""
        }

        val userdata =
            UserData(
                id = null,
                fbID = uID,
                email = email!!,  //  파이어베이스에서 가져오기
                nickname = userNicknameInput,
                addressMain = addressMain,
                addressDetail = addressDetail,
                reportPoint = 0,   //  모듈 만들기
                latitude = coordinateData.latitude,
                longitude = coordinateData.longitude,
                createdAt = timeStamp,
                fcm = ""
            )

        dbAccessModule.joinUser(userdata)
    }
}
