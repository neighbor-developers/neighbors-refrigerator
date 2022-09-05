package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.utilities.App
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
import com.neighbor.neighborsrefrigerator.viewmodels.ReqPostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DBAccessModule {
    private val dbAccessApi:  DBAccessInterface = DBApiClient.getApiClient().create()

    suspend fun checkNickname(nickname: String): Boolean{
        val result = kotlin.runCatching {
            dbAccessApi.checkNickname(nickname)}.getOrNull()?.result?: false
        //false 가 중복 ㄴㄴ
        return result
    }

    fun completeTrade(postData: PostData){
        dbAccessApi.completeTrade(postData).enqueue(object : Callback<ReturnObject<Int>>{
            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                if(response.isSuccessful){
                    Log.d("test",response.body()!!.msg)
                }
                else{
                    /**/
                }
            }

            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    //id로 포스트 데이터 불러오기
    fun getPostByUserId(userId :Int, applyPostDatas: (ArrayList<PostData>) -> Unit) {
        dbAccessApi.getPostByUserId(userId).enqueue(object :
            Callback<ReturnObject<ArrayList<PostData>>> {
            override fun onResponse(call: Call<ReturnObject<ArrayList<PostData>>>, response: Response<ReturnObject<ArrayList<PostData>>>) {
                if(response.isSuccessful){
                    applyPostDatas(response.body()!!.result)
                }
                else{
                    /*no-op*/
                }
            }

            override fun onFailure(call: Call<ReturnObject<ArrayList<PostData>>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

        })
    }
    //id로 포스트 데이터 불러오기
    fun getPostByPostId(postId :Int, applyPostData: (ArrayList<PostData>) -> Unit) {
        dbAccessApi.getPostById(postId).enqueue(object :Callback<ReturnObject<ArrayList<PostData>>>{
            override fun onResponse(
                call: Call<ReturnObject<ArrayList<PostData>>>,
                response: Response<ReturnObject<ArrayList<PostData>>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        applyPostData(it.result)
                    }
                }
            }

            override fun onFailure(call: Call<ReturnObject<ArrayList<PostData>>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    //유저 데이터 데이터 베이스에 입력
    fun joinUser(userData: UserData){
        dbAccessApi.userJoin(userData).enqueue(object : Callback<ReturnObject<Int>>{
            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                if(response.isSuccessful){
                    userData.id = response.body()?.result!!.toInt()
                    // sharedPreference에 저장하기
                    UserSharedPreference(App.context()).setUserPrefs(userData)

                    UserSharedPreference(App.context()).getUserPrefs("id")?.let { Log.d("성공", it) }
                    Log.d("DB 값", userData.toString())
                }
                else{
                    Log.d("test","join user response failed")
                }
            }

            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    //포스트 데이터 데이터베이스에 입력
    fun entryPost(postData: PostData, resultCode: (Int) -> Unit){
        dbAccessApi.entryPost(postData).enqueue(object : Callback<ReturnObject<Int>>{
            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                if(response.isSuccessful) {
                    Log.d("test","entry successful")
                    response.body()?.let {
                        resultCode(it.resultCode)
                    }
                }
                else{
                    Log.d("test","entry post response failed")
                }
            }

            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    suspend fun getPostOrderByTime(page: Int, reqPostData: ReqPostData) : List<PostData>{
        Log.d("실헹", "실행")
        val resultPosts = kotlin.runCatching {
            dbAccessApi.getPostOrderByTime(page, reqPostData.reqType, reqPostData.postType, reqPostData.categoryId, reqPostData.title, reqPostData.currentTime, reqPostData.latitude, reqPostData.longitude)
        }.getOrNull()?.result ?: emptyList()

        Log.d("결과", resultPosts.toString())
        return resultPosts
    }

    fun updateFcmToken(dataTransferObject: DataTransferObject<String>){
            dbAccessApi.updateFcmToken(dataTransferObject).enqueue(object : Callback<ReturnObject<Int>>{
                override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                    Log.d("test",t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ReturnObject<Int>>,
                    response: Response<ReturnObject<Int>>
                ) {
                    if(response.isSuccessful){
                        Log.d("test",response.body()!!.msg)
                    }
                }
            })
    }

    //review 등록하기
    fun reviewPost(id : Int, review: String, rate:Int){
        val reviewData = ReviewData(id,review,rate)
        dbAccessApi.reviewPost(reviewData).enqueue(object : Callback<ReturnObject<Int>>{
            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                if(response.isSuccessful){
                    Log.d("test",response.body()!!.msg)
                }

            }

            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    //id로 UserData 불러오기
//    suspend fun getUserInfoById(id: Int) : ArrayList<UserData>{
//        val result = kotlin.runCatching {
//            dbAccessApi.getUserInfoById(id)}.getOrNull()?.result?: arrayListOf()
//        return result
//    }
    //fbid로 UserData 불러오기
    suspend fun getUserInfoById(fbId: String) : ArrayList<UserData>{
        val result = kotlin.runCatching {
            dbAccessApi.getUserInfoByFbId(fbId)}.getOrNull()?.result?: arrayListOf()
        return result
    }

    //fb아이디에 등록된 유저 아이디인지 확인
    fun hasFbId(id : String, applyResult: (Boolean) -> Unit){
        dbAccessApi.hasFbId(id).enqueue(object :
            Callback<ReturnObject<Boolean>>{
                override fun onResponse(
                    call: Call<ReturnObject<Boolean>>,
                    response: Response<ReturnObject<Boolean>>
                ) {
                    if(response.isSuccessful){
                        applyResult(response.body()!!.result)
                    }
                }

                override fun onFailure(call: Call<ReturnObject<Boolean>>, t: Throwable) {
                    Log.d("test",t.localizedMessage)
                }
            }
        )
    }

    fun updateNickname(id : Int, nickname: String){
        val nicknameData = DataTransferObject<String>(id,nickname)
        dbAccessApi.updateNickname(nicknameData).enqueue(object : Callback<ReturnObject<Int>>{
            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                if(response.isSuccessful){
                    Log.d("test",response.body()!!.msg)


                }

            }

            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }
}