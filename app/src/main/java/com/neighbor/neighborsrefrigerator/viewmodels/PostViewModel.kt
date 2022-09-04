package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
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

    private val userLat = UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble()
    private val userLng = UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble()

    var timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))

    private var initData = MutableStateFlow(ReqPostData(3, 1, null, null, timeStamp, 37.3402, 126.7335))
    private val initDataSeek = MutableStateFlow(ReqPostData(3, 2, null, null, timeStamp, 37.3402, 126.7335))

    var sharePostsByTime = Pager(
                PagingConfig(pageSize = 15))
            { MyPagingSource(initData.value)
                }.flow.cachedIn(viewModelScope)

    var seekPostsByTime = Pager(
        PagingConfig(pageSize = 20))
    { MyPagingSource(initDataSeek.value)
    }.flow.cachedIn(viewModelScope)

    var sharePostsByDistance = MutableStateFlow<ArrayList<PostData>?>(null)

    fun initPostData(){
        sharePostsByTime = Pager(
            PagingConfig(pageSize = 15))
        { MyPagingSource(initData.value)
        }.flow.cachedIn(viewModelScope)
    }

    fun getPostForCategory(categoryId: Int){
        sharePostsByTime = Pager(
            PagingConfig(pageSize = 20)
        )
        { MyPagingSource(
            ReqPostData(1, postType =1, categoryId, null, currentTime = timeStamp, 37.3402, 126.7335)
        )
        }.flow.cachedIn(viewModelScope)
    }

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
