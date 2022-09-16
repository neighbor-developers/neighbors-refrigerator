package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.network.MyPagingSource
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalLevel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    private val userLat = UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble()
    private val userLng = UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble()

    var timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))

    private var initData = MutableStateFlow(ReqPostData(3, 1, null, null, timeStamp, userLat, userLng))
    private val initDataSeek = MutableStateFlow(ReqPostData(3, 2, null, null, timeStamp, userLat, userLng))

    var sharePostsByTime = Pager(
                PagingConfig(pageSize = 10))
            { MyPagingSource(initData.value)
                }.flow.cachedIn(viewModelScope)

    var seekPostsByTime = Pager(
        PagingConfig(pageSize = 10))
    { MyPagingSource(initDataSeek.value)
    }.flow.cachedIn(viewModelScope)


    var sharePostsByDistance = MutableStateFlow<List<PostData>?>(null)

    init {
        viewModelScope.launch {
            if (userLat != null && userLng != null) {
                sharePostsByDistance.value = dbAccessModule.getPostOrderByDistance(timeStamp, userLat, userLng)
            }
        }
    }


    suspend fun getUserData(userId: Int): UserData? {
        var userData : UserData? = null

        viewModelScope.async {
            userData = dbAccessModule.getUserInfoById(userId)[0]
        }.await()

        return userData
    }

    suspend fun getUserLevel(userId: Int): Int {
        var posts: ArrayList<PostData> = arrayListOf()
        viewModelScope.async {
            dbAccessModule.getPostByUserId(userId) { posts = it }
        }.await()
        val calLevel = CalLevel()

        return calLevel.GetUserLevel(posts)
    }

    fun changeTime(){
        timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(System.currentTimeMillis()))
    }
    // 판매완료 요청
    fun completeShare(postData: PostData){
        dbAccessModule.completeTrade(postData)
    }

}
