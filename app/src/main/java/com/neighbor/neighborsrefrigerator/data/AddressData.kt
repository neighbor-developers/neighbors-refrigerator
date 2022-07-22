package com.neighbor.neighborsrefrigerator.data

import com.neighbor.neighborsrefrigerator.network.AddressInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class AddressDetail(
    val bSuccess: Boolean,
    val htReturnValue: HtReturnValue
) {
    data class HtReturnValue(
        val paging: Paging,
        val result: Result
    ) {
        data class Paging(
            val blockSize: Int,
            val current: Int,
            val rowsPerPage: Int,
            val totalCount: Int
        )

        data class Result(
            val regionalTotalZipcodeSearchCount: Int,
            val regionalZipcodeStatusList: List<RegionalZipcodeStatus>,
            val totalZipcodeSearchCount: Int,
            val zipcodes: List<Zipcode>
        ) {
            data class RegionalZipcodeStatus(
                val regionCode: String,
                val regionName: String,
                val regionalZipcodeCounts: Int
            )
            data class Zipcode(
                val addressByNumberOfLot: String, //  일반 주소
                val addressByRoadName: String, //  도로명 주소
                val baseAddress: String,
                val isRoadNameAddress: Boolean,
                val massiveAddress: String,
                val roadZipcode: String,
                val zipcode: String
            )
        }
    }
}

val addressRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://order.pay.naver.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object APiObject {
    val retrofitService: AddressInterface by lazy {
        addressRetrofit.create(AddressInterface::class.java)
    }
}

fun AddressAdapter(addressDetail: List<AddressDetail.HtReturnValue.Result.Zipcode>): List<String> {
    // 데이터 정제

    var generalAddressList: List<String> = arrayListOf()
    var roadNameAddressList: List<String> = arrayListOf()
    // 도로명 주소가 추후에 필요하면 추가

    for (m in addressDetail) {
        generalAddressList = generalAddressList.plus(m.addressByNumberOfLot)
    }
    return generalAddressList
}
