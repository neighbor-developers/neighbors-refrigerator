package com.neighbor.neighborsrefrigerator.network

import com.neighbor.neighborsrefrigerator.data.AddressDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressInterface {
    @GET("zipcodeV2/extendedSearch?")
    fun getAddress(
        @Query("searchKeyword") searchKeyword: String
    ): retrofit2.Call<AddressDetail>
}
