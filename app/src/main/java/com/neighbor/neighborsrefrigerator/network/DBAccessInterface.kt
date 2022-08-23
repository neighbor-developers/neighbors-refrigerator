package com.neighbor.neighborsrefrigerator.network

import com.neighbor.neighborsrefrigerator.data.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DBAccessInterface {
    @GET("/post/getPostByUserId")
    fun getPostByUserId(
        @Query("userId") userId: Int
    ): retrofit2.Call<ReturnObject<ArrayList<PostData>>>

    @GET("/post/getPostOrderByTime")
    suspend fun getPostOrderByTime(
        @Query("page") page: Int,
        @Query("reqType") reqType: Int,
        @Query("postType") postType: Int,
        @Query("categoryId") categoryId: Int?,
        @Query("title") title: String?,
        @Query("currentTime") currentTime: String,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): ReturnObject<ArrayList<PostData>>

    @POST("/user/updateFcmToken")
    fun updateFcmToken(
        @Body dataTransferObject: DataTransferObject<String>
    ): retrofit2.Call<ReturnObject<Int>>

    @GET("/post/getInfoById")
    fun getPostById(
        @Query("id") id : Int
    ): retrofit2.Call<ReturnObject<ArrayList<PostData>>>

    @POST("/post/completeTrade")
    fun completeTrade(
        @Body postData : PostData
    ): retrofit2.Call<ReturnObject<Int>>

    @POST("/post/create")
    fun entryPost(
        @Body postData: PostData
    ): retrofit2.Call<ReturnObject<Int>>

    @POST("/post/review")
    fun reviewPost(
        @Body reviewData: ReviewData
    ): retrofit2.Call<ReturnObject<Int>>

    @GET("/user/getInfoById")
    suspend fun getUserInfoById(
        @Query("id") id : Int
    ) : ReturnObject<ArrayList<UserData>>

    @POST("/user/join")
    fun userJoin(
        @Body userData: UserData
    ): retrofit2.Call<ReturnObject<Int>>

    @GET("/user/checkNickname")
    fun checkNickname(
        @Query("nickname") nickname: String
    ): retrofit2.Call<ReturnObject<Boolean>>

    @GET("/user/hasFbId")
    fun hasFbId(
        @Query("id") id: String
    ): retrofit2.Call<ReturnObject<Boolean>>

    @POST("post/updateFcmToken")
    fun updateFcmToken(
        @Body userdata : UserData
    ) : retrofit2.Call<ReturnObject<Int>>

    @POST("/user/updateNickname")
    fun updateNickname(
        @Query("id") id: Int,
        @Query("nickname") nickname: String
    ) : retrofit2.Call<ReturnObject<Int>>



}