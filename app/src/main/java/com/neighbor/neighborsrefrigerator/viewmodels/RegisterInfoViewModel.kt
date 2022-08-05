package com.neighbor.neighborsrefrigerator.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessInterface
import com.neighbor.neighborsrefrigerator.network.DBApiClient
import com.neighbor.neighborsrefrigerator.scenarios.main.MainActivity
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.UseGeocoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.*

// textfield에 들어와 있는 데이터 저장
// nickname의 경우 변화가 감지될 경우 뷰모델에서 사용할 수 있는지 확인 후 true false 반환해 아이콘 색 변경해주기
// address의 경우 dialog에서 선택한 address를 클릭 시 textfield의 address에 자동으로 넣기
// dialog에서 address 검색 구성하기

class RegisterInfoViewModel(): ViewModel() {
    var userNicknameInput by mutableStateOf("")
    var addressMain by mutableStateOf("")
    var addressDetail by mutableStateOf("")
    val availableNickname = MutableStateFlow<Boolean?>(false)
    val fillAddressMain = MutableStateFlow<Boolean?>(false)

    var buttonEnabled = MutableStateFlow<Boolean?>(false)

    fun check(){
        buttonEnabled.value = availableNickname.value!!
    }


    val time = System.currentTimeMillis()
    private val timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(time))

    fun checkNickname(){
        // 0일때만 등록 ->
        // 1일때는 거부 ->
        if (userNicknameInput.isEmpty()){
            availableNickname.value = false
            return
        }
        val dbAccessApi: DBAccessInterface = DBApiClient.getApiClient().create()

        dbAccessApi.checkNickname(userNicknameInput).enqueue(object :
            Callback<ReturnObject<Boolean>> {
            override fun onResponse(
                call: Call<ReturnObject<Boolean>>,
                response: Response<ReturnObject<Boolean>>
            ) {
                availableNickname.value = !response.body()!!.result
                buttonEnabled.value = availableNickname.value!! && fillAddressMain.value!!
            }
            override fun onFailure(call: Call<ReturnObject<Boolean>>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }
        })
    }


    // db 모듈 만들기
    fun registerPersonDB(){

        val coordinateData: LatLng = UseGeocoder().addressToLatLng(addressMain + addressDetail)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val uID:String = if(auth.uid!=null){
            auth.uid!!
        } else{ "" }
        val email: String? = if(auth.currentUser?.email !=null){
            auth.currentUser!!.email
        } else{ "" }


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
                createdAt = timeStamp
            )

        val dbAccessApi: DBAccessInterface = DBApiClient.getApiClient().create()

        dbAccessApi.userJoin(
            userdata!!
        ).enqueue(object : Callback<ReturnObject<Int>> {

            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                userdata.id = response.body()?.result!!.toInt()
                // sharedPreference에 저장하기
                UserSharedPreference(App.context()).setUserPrefs(userdata)


                UserSharedPreference(App.context()).getUserPrefs("id")?.let { Log.d("성공", it) }
                Log.d("DB 값", userdata.toString())
            }
            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }
        })
    }
}
