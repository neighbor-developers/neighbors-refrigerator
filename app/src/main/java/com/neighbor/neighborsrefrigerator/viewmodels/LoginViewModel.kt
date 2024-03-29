package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.UseGeocoder
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LoginViewModel : ViewModel() {
    private val dbAccessModule = DBAccessModule()
    private lateinit var googleSignInClient : GoogleSignInClient

    // 로그인 결과 반환 변수
    private val _loginResult = MutableSharedFlow<Boolean>()
    var loginResult = _loginResult.asSharedFlow()

    suspend fun hasId(user : FirebaseUser): Boolean{
        var hasId = false

        viewModelScope.async {
            hasId = dbAccessModule.hasFbId(user.uid)
            Log.d("hasId", hasId.toString())
        }.await()

        if (hasId){
            val userData = dbAccessModule.getUserInfoByFbId(user.uid)[0]
            UserSharedPreference(App.context()).setUserPrefs(userData)
        }

        return hasId
    }

    fun tryLogin(context: Context) {
        Log.d("로그인중", "로그인중")
        viewModelScope.launch {
            val account = async {
                getLastSignedInAccount(context)
            }
            delay(2500)
            // 계정 확인 -> true, 없음 -> false 반환
            setLoginResult(account.await() != null)
        }
    }

    // 이전에 로그인 한 계정이 있는지 확인
    private fun getLastSignedInAccount(context: Context) =
        GoogleSignIn.getLastSignedInAccount(context)

    fun setLoginResult(isLogin: Boolean) {
        viewModelScope.launch {
            _loginResult.emit(isLogin)
        }
    }

    // 계정등록

    var userNicknameInput by mutableStateOf("")
    var addressMain = MutableStateFlow("")
    var addressDetail = MutableStateFlow("")
    val isOverlap = MutableStateFlow(true)
    val isEmpty = MutableStateFlow(false)
    val availableNickname = MutableStateFlow(false)
    val fillAddressMain = MutableStateFlow(false)

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()


    val time = System.currentTimeMillis()
    private val timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(
        Date(time)
    )

    suspend fun checkNickname() {
        if (userNicknameInput.isEmpty()) {
            isEmpty.value = true
            availableNickname.value = false
        }else{
            isEmpty.value = false
            val result = dbAccessModule.checkNickname(userNicknameInput)
            availableNickname.value = if (!result) {
                isOverlap.value = true
                true
            }else{
                isOverlap.value = false
                false
            }}
    }

    fun registerPersonDB() {
        val coordinateData: LatLng = UseGeocoder().addressToLatLng(addressMain.value + addressDetail.value)
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
                addressMain = addressMain.value,
                addressDetail = addressDetail.value,
                reportPoint = 0,   //  모듈 만들기
                latitude = coordinateData.latitude,
                longitude = coordinateData.longitude,
                createdAt = timeStamp,
                fcm = ""
            )
        viewModelScope.launch {
            userdata.fcm  = UserSharedPreference(App.context()).getUserPrefs("fcm")
            val result = dbAccessModule.joinUser(userdata)
            Log.d("id", result.toString())
            userdata.id = result

            UserSharedPreference(App.context()).setUserPrefs(userdata)
            Log.d("저장", userdata.toString())
            UserSharedPreference(App.context()).setLevelPref("flowerVer", 1)
        }
    }
    fun toMainActivity() = viewModelScope.launch { _event.emit(LoginEvent.ToMain) }

    sealed class LoginEvent{
        object ToMain : LoginEvent()
    }

}
