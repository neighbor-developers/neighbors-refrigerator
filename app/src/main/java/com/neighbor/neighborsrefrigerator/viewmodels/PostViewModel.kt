package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.google.firebase.auth.FirebaseAuth
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.network.MyPagingSource
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ReqPostData(
    val reqType: Int,
    val postType: Int,
    val categoryId: Int?,
    val title: String?,
    val currentTime: String,
    val latitude: Double?,
    val longitude: Double?
)

class PostViewModel : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    private val auth = FirebaseAuth.getInstance()

    private val userLat = UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble()
    private val userLng = UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble()

    var timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))

    private var initData = MutableStateFlow(ReqPostData(3, 1, null, null, timeStamp, userLat, userLng))
    private val initDataSeek = MutableStateFlow(ReqPostData(3, 2, null, null, timeStamp, userLat, userLng))

    val category = MutableStateFlow<Int>(0)

    var sharePostsByTime = Pager(
                PagingConfig(pageSize = 15))
            { MyPagingSource(initData.value)
                }.flow.cachedIn(viewModelScope)

    var seekPostsByTime = Pager(
        PagingConfig(pageSize = 20))
    { MyPagingSource(initDataSeek.value)
    }.flow.cachedIn(viewModelScope)

    var sharePostsForCategory100 = Pager(
        PagingConfig(pageSize = 15))
    { MyPagingSource(ReqPostData(1, 1, 100, null, timeStamp, userLat, userLng))
    }.flow.cachedIn(viewModelScope)

    var sharePostsForCategory200 = Pager(
        PagingConfig(pageSize = 15))
    { MyPagingSource(ReqPostData(1, 1, 200, null, timeStamp, userLat, userLng))
    }.flow.cachedIn(viewModelScope)

    var sharePostsForCategory300 = Pager(
        PagingConfig(pageSize = 15))
    { MyPagingSource(ReqPostData(1, 1, 300, null, timeStamp, userLat, userLng))
    }.flow.cachedIn(viewModelScope)

    var sharePostsForCategory400 = Pager(
        PagingConfig(pageSize = 15))
    { MyPagingSource(ReqPostData(1, 1, 400, null, timeStamp, userLat, userLng))
    }.flow.cachedIn(viewModelScope)

    var sharePostsForCategory500 = Pager(
        PagingConfig(pageSize = 15))
    { MyPagingSource(ReqPostData(1, 1, 500, null, timeStamp, userLat, userLng))
    }.flow.cachedIn(viewModelScope)

    var sharePostsForCategory600 = Pager(
        PagingConfig(pageSize = 15))
    { MyPagingSource(ReqPostData(1, 1, 600, null, timeStamp, userLat, userLng))
    }.flow.cachedIn(viewModelScope)

    var sharePostsByDistance = MutableStateFlow<ArrayList<PostData>?>(null)

    suspend fun getUserNickname(userId: Int): UserData? {
        var userData : UserData? = null

        viewModelScope.async {
            userData = dbAccessModule.getUserInfoById(userId)[0]
        }.await()

        return userData
    }

    fun changeTime(){
        timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(System.currentTimeMillis()))
    }

}
