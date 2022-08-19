package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.database.FirebaseDatabase
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.RdbChatData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.network.MyPagingRepository
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
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
    private val myPagingRepository = MyPagingRepository()

    private val userLat = UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble()
    private val userLng = UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble()

    var timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).format(Date(System.currentTimeMillis()))

    var sharePostsByTime: Flow<PagingData<PostData>> = myPagingRepository.getPostsByTime(
        ReqPostData(3, 1, null, null, timeStamp, userLat, userLng)
    ).cachedIn(viewModelScope)

    var seekPostsByTime: Flow<PagingData<PostData>> = myPagingRepository.getPostsByTime(
        ReqPostData(3, 2, null, null, timeStamp, userLat, userLng)
    ).cachedIn(viewModelScope)// 변경상태에도 페이징 상태를 유지하기 위해

    var searchedPosts: Flow<PagingData<PostData>> = flow{}

    var sharePostsByDistance = MutableStateFlow<ArrayList<PostData>?>(null)


    fun getPosts(item: String?, category:Int?, reqType: String, postType: String, varType: Int) {
        viewModelScope.launch {
            val result = myPagingRepository.getPostsByTime(
                ReqPostData(
                    reqType = when (reqType) {
                        "category" -> 1
                        "search" -> 2
                        "justTime" -> 3
                        else -> 1
                    },

                    postType = when (postType) {
                        "share" -> 1
                        "seek" -> 2
                        else -> 1
                    },
                    categoryId = category, // null일수 있음
                    title = item,
                    currentTime = timeStamp,
                    133.4, 80.6
                )
            )
            when(varType){
                1 -> {sharePostsByTime = result}
                2 -> {seekPostsByTime = result}
                3 -> {searchedPosts = result}
            }
        }
    }
    fun changeTime(){
        timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(System.currentTimeMillis()))
    }

}
