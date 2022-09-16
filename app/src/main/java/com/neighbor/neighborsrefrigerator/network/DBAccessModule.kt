package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import com.neighbor.neighborsrefrigerator.data.*
import com.neighbor.neighborsrefrigerator.viewmodels.ReqPostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DBAccessModule {
    private val dbAccessApi:  DBAccessInterface = DBApiClient.getApiClient().create()

    suspend fun checkNickname(nickname: String): Boolean{
        val result = kotlin.runCatching {
            dbAccessApi.checkNickname(nickname)}.getOrNull()?.result?: true
        Log.d("닉네임 결과", result.toString())
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
    suspend fun joinUser(userData: UserData): Int?{
        val result = kotlin.runCatching {
            dbAccessApi.userJoin(userData)}.getOrNull()?.result
        Log.d("id", result.toString())
        return result
    }

    fun sendMail(mailData: MailData){
        dbAccessApi.sendMail(mailData).enqueue(object : Callback<ReturnObject<Int>>{
            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                if(response.isSuccessful){
                    Log.d("메일 결과 : ", response.toString())
                }

            }

            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }
        })
    }

    //포스트 데이터 데이터베이스에 입력
    suspend fun entryPost(postData: PostData): Int {
        val response = kotlin.runCatching {
            dbAccessApi.entryPost(postData)
        }.getOrNull()?.result ?: 0
        Log.d("결과", response.toString())

        return response
    }

    suspend fun getPostOrderByTime(page: Int, reqPostData: ReqPostData) : List<PostData>{
        Log.d("실헹", "실행")
        val resultPosts = kotlin.runCatching {
            dbAccessApi.getPostOrderByTime(page, reqPostData.reqType, reqPostData.postType, reqPostData.categoryId, reqPostData.title, reqPostData.currentTime, reqPostData.latitude, reqPostData.longitude)
        }.getOrNull()?.result ?: emptyList()

        //Log.d("결과", resultPosts.toString())
        return resultPosts
    }

    //거리도 distance로 반환하게 만들었는데 잘 될지는 모르겠네요? 일단 PostData 객체 distance 속성으로 값 들어가게 했어요
    suspend fun getPostOrderByDistance(currentTime:String, latitude:Double, longitude: Double) : List<PostData>{
        Log.d("실헹", "실행")
        val resultPosts = kotlin.runCatching {
            dbAccessApi.getPostOrderByDistance(currentTime,latitude,longitude)
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
    suspend fun getUserInfoById(id: Int) : ArrayList<UserData>{
        val result = kotlin.runCatching {
            dbAccessApi.getUserInfoById(id)}.getOrNull()?.result?: arrayListOf()
        return result
    }
    //fbid로 UserData 불러오기
    suspend fun getUserInfoByFbId(fbId: String) : ArrayList<UserData>{
        val result = kotlin.runCatching {
            dbAccessApi.getUserInfoByFbId(fbId)}.getOrNull()?.result?: arrayListOf()
        Log.d("userInfo", result.toString())
        return result
    }

    //fb아이디에 등록된 유저 아이디인지 확인
    suspend fun hasFbId(id : String): Boolean{
        val result = kotlin.runCatching {
            dbAccessApi.hasFbId(id)}.getOrNull()?.result?: false
        Log.d("checkLogin", result.toString())
        return result
    }


    suspend fun deleteUser(id : Int): Boolean{
        val result = kotlin.runCatching {
            dbAccessApi.deleteUser(id)}.getOrNull()?.result?: false
        Log.d("checkLogin", result.toString())
        return result
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
    fun updateUserLocation(id: Int, latitude:Double, longitude:Double, home_addr: String){
        dbAccessApi.updateUserLocation(id,latitude,longitude,home_addr).enqueue(object: Callback<ReturnObject<Int>>{
            override fun onFailure(call: Call<ReturnObject<Int>>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

            override fun onResponse(
                call: Call<ReturnObject<Int>>,
                response: Response<ReturnObject<Int>>
            ) {
                Log.d("test", response.body().toString())
            }
        })
    }
    fun makeChat(id:Int, postId:Int, contactId:Int, createdAt:String){
        val chatData = ChatData(id,postId,contactId,createdAt)
        dbAccessApi.makeChat(chatData).enqueue(object : Callback<ReturnObject<Int>>{
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

    fun sendMessage(chatId:String, createdAt:String, content:String, from:Int){
        val messageData = MessageData(chatId, createdAt ,content,from)
        dbAccessApi.sendMessage(messageData).enqueue(object : Callback<ReturnObject<Int>>{
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