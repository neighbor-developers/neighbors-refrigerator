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
    private val dbAccessApi:  DBAccessInterface = DBApiClient.getApiClient().create()
    fun getPostByUserId(userId :Int, applyPostDatas: (ArrayList<PostData>) -> Unit) {
        dbAccessApi.getPostByUserId(userId).enqueue(object :
            Callback<ReturnObjectForPost> {
            override fun onResponse(call: Call<ReturnObjectForPost>, response: Response<ReturnObjectForPost>) {
                if(response.isSuccessful){
                    applyPostDatas(response.body()!!.result)
                }
                else{

                }
            }

            override fun onFailure(call: Call<ReturnObjectForPost>, t: Throwable) {
                Log.d("test",t.localizedMessage)
            }

        })
    }

    fun getPostOrderByTime(reqType :Int, postType: Int, currentIndex: Int, num: Int, categoryId: Int?, productName: String?, currentTime: String, applyPostDatas: (ArrayList<PostData>) -> Unit) {
        dbAccessApi.getPostOrderByTime(reqType, postType, currentIndex, num, categoryId, productName, currentTime).enqueue(object :
            Callback<ReturnObjectForPost> {
            override fun onResponse(call: Call<ReturnObjectForPost>, response: Response<ReturnObjectForPost>) {
                if(response.isSuccessful){
                    applyPostDatas(response.body()!!.result)
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