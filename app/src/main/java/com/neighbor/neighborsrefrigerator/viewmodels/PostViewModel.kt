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

    var sharePostsByTime = Pager(
                PagingConfig(pageSize = 15))
            { MyPagingSource(initData.value)
                }.flow.cachedIn(viewModelScope)

    var seekPostsByTime = Pager(
        PagingConfig(pageSize = 20))
    { MyPagingSource(initDataSeek.value)
    }.flow.cachedIn(viewModelScope)


    var sharePostsByDistance = MutableStateFlow<ArrayList<PostData>?>(arrayListOf(
        PostData(id=312, title="제목301", categoryId="200", userId=2, content="내용301", type=2, mainAddr="경기도시흥시301", addrDetail="301호", rate=0, review=null, validateType=1, validateDate="2022-08-22T00:00:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null),
        PostData(id=312, title="제목301", categoryId="200", userId=2, content="내용301", type=2, mainAddr="경기도시흥시301", addrDetail="301호", rate=0, review=null, validateType=1, validateDate="2022-08-22T00:00:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null),
                PostData(id=312, title="제목301", categoryId="200", userId=2, content="내용301", type=2, mainAddr="경기도시흥시301", addrDetail="301호", rate=0, review=null, validateType=1, validateDate="2022-08-22T00:00:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null),
            PostData(id=312, title="제목301", categoryId="200", userId=2, content="내용301", type=2, mainAddr="경기도시흥시301", addrDetail="301호", rate=0, review=null, validateType=1, validateDate="2022-08-22T00:00:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null),
    PostData(id=312, title="제목301", categoryId="200", userId=2, content="내용301", type=2, mainAddr="경기도시흥시301", addrDetail="301호", rate=0, review=null, validateType=1, validateDate="2022-08-22T00:00:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null),
        PostData(id=312, title="제목301", categoryId="200", userId=2, content="내용301", type=2, mainAddr="경기도시흥시301", addrDetail="301호", rate=0, review=null, validateType=1, validateDate="2022-08-22T00:00:00.000Z", validateImg=null, productimg1="https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg", productimg2=null, productimg3=null, createdAt="2022-08-22T16:45:04.000Z", updatedAt="2022-08-22T16:47:28.000Z", completedAt=null, latitude=37.34, longitude=126.73, state="1", distance=null)

    ))


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
    // 판매완료 요청
    fun completeShare(postData: PostData){
        dbAccessModule.completeTrade(postData)
    }

}
