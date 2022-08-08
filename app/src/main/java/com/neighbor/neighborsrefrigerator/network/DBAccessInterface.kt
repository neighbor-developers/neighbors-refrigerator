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
    fun getPostOrderByTime(
        @Query("reqType") reqType: Int,
        @Query("postType") postType: Int,
        @Query("currentIndex") currentIndex: Int,
        @Query("num") num: Int,
        @Query("categoryId") categoryId: Int?,
        @Query("title") title: String?,
        @Query("currentTime") currentTime: String,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): retrofit2.Call<ReturnObject<ArrayList<PostData>>>

    @POST("/post/create")
    fun entryPost(
        @Body postData: PostData
    ): retrofit2.Call<ReturnObject<Int>>

    @POST("/post/review")
    fun reviewPost(
        @Body reviewData: ReviewData
    ): retrofit2.Call<ReturnObject<Int>>

    @GET("/user/getInfoById")
    fun getUserInfoById(
        @Query("id") id : Int
    ) : retrofit2.Call<ReturnObject<ArrayList<UserData>>>

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
}