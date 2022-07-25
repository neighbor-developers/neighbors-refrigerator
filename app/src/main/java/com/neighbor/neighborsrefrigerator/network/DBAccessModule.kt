package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.ReturnObjectForPost
import com.neighbor.neighborsrefrigerator.utilities.CalDistance
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
                if(response.isSuccessful){
                   val test =  response.body()!!.result[0]
                    Log.d("test",test.title!!)
                }
                else{

                }
            }

            override fun onFailure(call: Call<ReturnObjectForPost>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

        })
    }
}