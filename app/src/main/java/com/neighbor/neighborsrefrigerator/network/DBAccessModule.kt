package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
import com.neighbor.neighborsrefrigerator.viewmodels.ReqPostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DBAccessModule {
    private val dbAccessApi:  DBAccessInterface = DBApiClient.getApiClient().create()

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
                    Log.d("test","joined successfully")
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
        val resultPosts = kotlin.runCatching {
            dbAccessApi.getPostOrderByTime(page, reqPostData.reqType, reqPostData.postType, reqPostData.categoryId, reqPostData.title, reqPostData.currentTime, reqPostData.latitude, reqPostData.longitude)
        }.getOrNull()?.result ?: emptyList()

        Log.d("결과", resultPosts.toString())
        return resultPosts
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
    suspend fun getUserInfoById(id: Int) : ArrayList<UserData>{
        val result = kotlin.runCatching {
            dbAccessApi.getUserInfoById(id)}.getOrNull()?.result?: arrayListOf()
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
        dbAccessApi.updateNickname(id, nickname).enqueue(object : Callback<ReturnObject<Int>>{
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

    fun updateFcmToken(userData: UserData){
        dbAccessApi.updateFcmToken(userData).enqueue(object : Callback<ReturnObject<Int>>{
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