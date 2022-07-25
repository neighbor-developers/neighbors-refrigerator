package com.neighbor.neighborsrefrigerator.network

import ReturnObjectForWrite
import com.neighbor.neighborsrefrigerator.data.AddressDetail
import com.neighbor.neighborsrefrigerator.data.ReturnObjectForPost
import com.neighbor.neighborsrefrigerator.data.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DBAccessInterface {
    @GET("/post/getPostByUserId")
    fun getPostByUserId(
        @Query("userId") userId: Int
    ): retrofit2.Call<ReturnObjectForPost>

    @GET("/post/getPostOrderByTime")
    fun getPostOrderByTime(
    ): retrofit2.Call<ReturnObjectForPost>

    @POST("/user/join")
    fun userJoin(
        @Body userData: UserData
    ): retrofit2.Call<ReturnObjectForWrite>
}