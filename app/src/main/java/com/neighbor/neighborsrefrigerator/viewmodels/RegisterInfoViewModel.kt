package com.neighbor.neighborsrefrigerator.viewmodels

import ReturnObjectForWrite
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessInterface
import com.neighbor.neighborsrefrigerator.network.DBApiClient
import com.neighbor.neighborsrefrigerator.scenarios.main.MainActivity
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.UseGeocoder
import kotlinx.coroutines.flow.MutableStateFlow
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
    val availableNickname = MutableStateFlow<Boolean?>(false)
    var addressMain by mutableStateOf("")
    var addressDetail by mutableStateOf("")

    val time = System.currentTimeMillis()
    val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(time))

    fun checkNickname(){
        // 0일때만 등록 ->
        // 1일때는 거부 ->
        val dbAccessApi: DBAccessInterface = DBApiClient.getApiClient().create()

        dbAccessApi.checkNickname(userNicknameInput).enqueue(object :
            Callback<ReturnObjectForNickname> {
            override fun onResponse(
                call: Call<ReturnObjectForNickname>,
                response: Response<ReturnObjectForNickname>
            ) {
                availableNickname.value = !response.body()!!.isExist
            }
            override fun onFailure(call: Call<ReturnObjectForNickname>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }
        })
    }


    // db 모듈 만들기
    fun registerPersonDB(){

        var coordinateData: CoordinateData = UseGeocoder().addressToLatLng(addressMain + addressDetail)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var uID:String = if(auth.uid!=null){
            auth.uid!!
        } else{ "" }
        val email: String? = if(auth.currentUser?.email !=null){
            auth.currentUser!!.email
        } else{ "" }


        val userdata =
            UserData(0,
                uID,
                email!!,  //  파이어베이스에서 가져오기
                userNicknameInput,
                addressMain,
                addressDetail,
                0,   //  모듈 만들기
                coordinateData.latitude,
                coordinateData.longitude,
                timeStamp
            )
        // sharedPreference에 저장하기
        UserSharedPreference(App.context()).setUserPrefs(userdata)

        val dbAccessApi: DBAccessInterface = DBApiClient.getApiClient().create()

        dbAccessApi.userJoin(
            userdata!!
        ).enqueue(object : Callback<ReturnObjectForWrite> {

            override fun onResponse(
                call: Call<ReturnObjectForWrite>,
                response: Response<ReturnObjectForWrite>
            ) {
                Log.d("성공", "PersonDB에 데이터 입력")
                Log.d("DB 값", userdata.toString())
            }
            override fun onFailure(call: Call<ReturnObjectForWrite>, t: Throwable) {
                Log.d("실패", t.localizedMessage)
            }
        })
    }
}
