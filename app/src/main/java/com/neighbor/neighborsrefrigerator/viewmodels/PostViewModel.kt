package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference
import com.neighbor.neighborsrefrigerator.network.DBAccessModule
import com.neighbor.neighborsrefrigerator.utilities.App
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*

class PostViewModel : ViewModel() {

    private val dbAccessModule = DBAccessModule()
    var sharePostsByTime = MutableStateFlow<ArrayList<PostData>?>(null)
    var sharePostsByDistance = MutableStateFlow<ArrayList<PostData>?>(null)
    var seekPostsByTime = MutableStateFlow<ArrayList<PostData>?>(null)
    var searchedPosts = MutableStateFlow<ArrayList<PostData>?>(null)

    var timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(System.currentTimeMillis()))

//    private val userLat = UserSharedPreference(App.context()).getUserPrefs("latitude")?.toDouble()
//    private val userLng = UserSharedPreference(App.context()).getUserPrefs("longitude")?.toDouble()

    init {
        dbAccessModule.getPostOrderByTime(3, 1,0, 12, null, null, timeStamp, 133.4, 80.6) { sharePostsByTime.value = it }
        dbAccessModule.getPostOrderByTime(3, 2, 0, 12, null, null, timeStamp, 144.3, 80.6) { seekPostsByTime.value = it }
    }

    fun getPosts(item: String?, category:Int?, reqType: String, postType: String, currentIndex: Int, num: Int, applyPostData : (ArrayList<PostData>) -> Unit){

        dbAccessModule.getPostOrderByTime(
            reqType = when(reqType){
                "category" -> 1
                "search" -> 2
                "justTime" -> 3
                else -> 1
            },

            postType = when(postType){
                "share" -> 1
                "seek" -> 2
                else -> 1
            },
            currentIndex = currentIndex,
            num = num,
            categoryId = category, // null일수 있음
            title = item,
            currentTime = timeStamp,
            133.4, 80.6
            )
        {
            applyPostData(it)
        }

    }

    fun changeTime(){
        timeStamp = SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(Date(System.currentTimeMillis()))
    }
}
