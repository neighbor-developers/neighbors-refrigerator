package com.neighbor.neighborsrefrigerator.network

import ReturnObjectForWrite
import android.util.Log
import android.widget.Toast
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DBAccessModule {
    private val dbAccessApi:  DBAccessInterface = DBApiClient.getApiClient().create()


    //id로 포스트 데이터 불러오기
    fun getPostByUserId(userId :Int, applyPostDatas: (ArrayList<PostData>) -> Unit) {
        dbAccessApi.getPostByUserId(userId).enqueue(object :
            Callback<ReturnObjectForPost> {
            override fun onResponse(call: Call<ReturnObjectForPost>, response: Response<ReturnObjectForPost>) {
                if(response.isSuccessful){
                    applyPostDatas(response.body()!!.result)
                }
                else{
                    /*no-op*/
                }
            }

            override fun onFailure(call: Call<ReturnObjectForPost>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

        })
    }

    //유저 데이터 데이터 베이스에 입력
    fun joinUser(userData: UserData){
        dbAccessApi.userJoin(userData).enqueue(object : Callback<ReturnObjectForWrite>{
            override fun onResponse(
                call: Call<ReturnObjectForWrite>,
                response: Response<ReturnObjectForWrite>
            ) {
                if(response.isSuccessful){
                    Log.d("test","joined successfully")
                }
                else{
                    Log.d("test","join user response failed")
                }
            }

            override fun onFailure(call: Call<ReturnObjectForWrite>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }
    // 리뷰 작성 (포스트 데이터 데이터 베이스 rate, review 입력)
    fun postReview(postId: Int, rate: Int, review: String){


    }

    //포스트 데이터 데이터베이스에 입력
    fun entryPost(postData: PostData){
        dbAccessApi.entryPost(postData).enqueue(object : Callback<ReturnObjectForWrite>{
            override fun onResponse(
                call: Call<ReturnObjectForWrite>,
                response: Response<ReturnObjectForWrite>
            ) {
                if(response.isSuccessful){
                    Log.d("test","entry successful")
                }
                else{
                    Log.d("test","entry post response failed")
                }
            }

            override fun onFailure(call: Call<ReturnObjectForWrite>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    //시간순으로 포스트 데이터 불러오기
    fun getPostOrderByTime(reqType :Int, postType: Int, currentIndex: Int, num: Int, categoryId: Int?, productName: String?, currentTime: String, applyPostDatas: (ArrayList<PostData>) -> Unit) {
        dbAccessApi.getPostOrderByTime(reqType, postType, currentIndex, num, categoryId, productName, currentTime).enqueue(object :
            Callback<ReturnObjectForPost> {
            override fun onResponse(call: Call<ReturnObjectForPost>, response: Response<ReturnObjectForPost>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        applyPostDatas(it.result)
                    }
                }
                else{
                    /*no-op*/
                }
            }

            override fun onFailure(call: Call<ReturnObjectForPost>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

        })
    }

    //fb아이디에 등록된 유저 아이디인지 확인
    fun hasFbId(id : String, applyResult: (Boolean) -> Unit){
        dbAccessApi.hasFbId(id).enqueue(object :
            Callback<ReturnObjectForHasFbId>{
                override fun onResponse(
                    call: Call<ReturnObjectForHasFbId>,
                    response: Response<ReturnObjectForHasFbId>
                ) {
                    if(response.isSuccessful){
                        applyResult(response.body()!!.hasfFb)
                    }
                }

                override fun onFailure(call: Call<ReturnObjectForHasFbId>, t: Throwable) {
                    Log.d("hasFb Failure",t.localizedMessage)
                }
            }
        )

    }



}