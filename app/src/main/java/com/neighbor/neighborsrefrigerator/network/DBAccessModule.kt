package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.ReturnObjectForPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DBAccessModule {
    val dbAccessApi:  DBAccessInterface = DBApiClient.getApiClient().create()
    fun getPostByUserId(userId :Int) {
        dbAccessApi.getPostByUserId(2).enqueue(object :
            Callback<ReturnObjectForPost> {
            override fun onResponse(call: Call<ReturnObjectForPost>, response: Response<ReturnObjectForPost>) {
                Log.d("test","onResponse")
            }

            override fun onFailure(call: Call<ReturnObjectForPost>, t: Throwable) {
                Log.d("test","onfailure")
            }

        })
    }
}