package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

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

}
