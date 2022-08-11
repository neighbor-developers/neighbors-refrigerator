package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
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

    //시간순으로 포스트 데이터 불러오기
//    fun getPostOrderByTime(reqType :Int, postType: Int, currentIndex: Int, num: Int, categoryId: Int?, title: String?, currentTime: String, latitude: Double?, longitude: Double?, applyPostDatas: (ArrayList<PostData>) -> Unit) {
//        dbAccessApi.getPostOrderByTime(reqType, postType, currentIndex, num, categoryId, title, currentTime, latitude, longitude).enqueue(object :
//            Callback<ReturnObject<ArrayList<PostData>>> {
//            override fun onResponse(call: Call<ReturnObject<ArrayList<PostData>>>, response: Response<ReturnObject<ArrayList<PostData>>>) {
//                if(response.isSuccessful){
//                    response.body()?.let {
//                        applyPostDatas(it.result)
//                    }
//                }
//                else{
//                    /*no-op*/
//                }
//            }
//
//            override fun onFailure(call: Call<ReturnObject<ArrayList<PostData>>>, t: Throwable) {
//                Log.d("test",t.localizedMessage)
//            }
//
//        })
//    }
    fun getPostOrderByTime(page: Int, pageSize:Int, reqType :Int, postType: Int, categoryId: Int?, title: String?, currentTime: String, latitude: Double?, longitude: Double?, applyPostDatas: (ArrayList<PostData>) -> Unit){
        dbAccessApi.getPostOrderByTime(page, pageSize, reqType, postType, categoryId, title, currentTime, latitude, longitude).enqueue(object :
            Callback<ReturnObject<ArrayList<PostData>>> {
            override fun onResponse(call: Call<ReturnObject<ArrayList<PostData>>>, response: Response<ReturnObject<ArrayList<PostData>>>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        applyPostDatas(it.result)
                    }
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
    fun getUserInfoById(id: Int, applyUserDatas: (ArrayList<UserData> /* = java.util.ArrayList<com.neighbor.neighborsrefrigerator.data.PostData> */) -> Unit){
        dbAccessApi.getUserInfoById(id).enqueue(object :
            Callback<ReturnObject<ArrayList<UserData>>> {
            override fun onResponse(call: Call<ReturnObject<ArrayList<UserData>>>, response: Response<ReturnObject<ArrayList<UserData>>>) {
                if(response.isSuccessful){
                    applyUserDatas(response.body()!!.result)
                }
                else{
                    /*no-op*/
                }
            }

            override fun onFailure(call: Call<ReturnObject<ArrayList<UserData>>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

        })
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


}