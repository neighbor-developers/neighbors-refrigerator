package com.neighbor.neighborsrefrigerator.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class retrofitClient {
    companion object {
        const val BASE_URL = "13.52.122.41"
    }

    val retrofitCli = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
