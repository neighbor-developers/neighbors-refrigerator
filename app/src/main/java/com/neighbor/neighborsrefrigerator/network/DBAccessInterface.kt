package com.neighbor.neighborsrefrigerator.network

import com.neighbor.neighborsrefrigerator.data.AddressDetail
import com.neighbor.neighborsrefrigerator.data.ReturnObjectForPost
import retrofit2.http.GET
import retrofit2.http.Query

interface DBAccessInterface {
    @GET("/post/getPostByUserId")
    fun getPostByUserId(
        @Query("userId") userId: Int
    ): retrofit2.Call<ReturnObjectForPost>

    @GET("/post/getPostOrderByTime")
    fun getPostOrderByTime(
    ): retrofit2.Call<ReturnObjectForPost>
}