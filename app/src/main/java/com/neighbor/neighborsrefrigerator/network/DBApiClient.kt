package com.neighbor.neighborsrefrigerator.network

import com.neighbor.neighborsrefrigerator.data.addressRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object DBApiClient {
        private const val BASE_URL = "http://ec2-13-52-122-41.us-west-1.compute.amazonaws.com:3000/"
        private var retrofit: Retrofit? = null

        fun getApiClient() : Retrofit {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retrofit!!
            }
    }


