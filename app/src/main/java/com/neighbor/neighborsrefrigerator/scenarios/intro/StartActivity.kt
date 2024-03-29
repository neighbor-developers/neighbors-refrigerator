package com.neighbor.neighborsrefrigerator.scenarios.intro

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.neighbor.neighborsrefrigerator.R
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.scenarios.main.MainActivity
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {
    // Firebase
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1313
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.backGreen)) {
                Image(painter = painterResource(id = R.drawable.back), contentDescription = "", modifier = Modifier.padding(20.dp))
//                Text(text = "로그인 확인중", fontSize = 30.sp, modifier = Modifier.background(color = colorResource(
//                    id = R.color.backGreen
//                )))
            }
        }
        // 로그인 시도
        viewModel.tryLogin(this)

        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.loginResult.collect { isLogin ->
                    if (isLogin) {
                        Log.d("로그인 되어있음", auth.currentUser.toString())
                        if (auth.currentUser != null) {
                            Log.d("token", auth.currentUser!!.getIdToken(true).toString())
                            val result = viewModel.hasId(auth.currentUser!!)
                            Log.d("아이디 있는지", result.toString())
                            Log.d("uid", auth.currentUser!!.uid.toString())
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }

                                // Get new FCM registration token
                                val token = task.result

                                // Log and toast
                                val msg = getString(R.string.msg_token_fmt, token)
                                Log.d("token!!", msg)
                                UserSharedPreference(App.context()).setUserPrefs("fcm", token)
                            })

                            if (result){
                                Log.d("아이디 있는지", "메인으로")
                                toMainActivity()
                            }else{
                                Log.d("아이디 없어서", "등록으로")
                                setContent {
                                    val navController = rememberNavController()


                                    // NavHost 로 네비게이션 결정
                                    NavHost(navController, "RegisterInfo")
                                    {
                                        composable("RegisterInfo") {
                                            RegisterInfo(navController = navController)
                                        }
                                        composable("Guide") {
                                            GuideScreen(viewModel)
                                        }
                                    }
                                }
                            }
                        }else{
                            Log.d("로그인 되어있음", "로그인 되어있지만? 유저가 없음")
                        }
                    } else {
                        Log.d("로그인 안되어있음", "로그인 안되어있음")
                        // 로그인 안되어있을 때 로그인 페이지 열림
                        setContent {
                            LoginScreen {
                                googleLogin()
                            }
                        }
                    }
                }
            }
            launch {
                viewModel.event.collect{ event ->
                    when (event) {
                        LoginViewModel.LoginEvent.ToMain -> toMainActivity()
                    }
                }
            }

        }

    }

    // 로그인 객체 생성
    private fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // 빨간줄이지만 토큰 문제라 실행 가능
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignIn()
    }

    // 구글 회원가입
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "구글 회원가입에 실패하였습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            /*no-op*/
        }
    }

    // account 객체에서 id 토큰 가져온 후 Firebase 인증
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            Log.d("로그인중", "로그인 되어야함1")
            if (task.isSuccessful) {
                Log.d("로그인중", "로그인 되어야함2")
                auth.currentUser?.let {
                    Log.d("로그인중", "로그인 되어야함3")
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.setLoginResult(true)
                    }
                }
            }
        }
    }


    private fun toMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
