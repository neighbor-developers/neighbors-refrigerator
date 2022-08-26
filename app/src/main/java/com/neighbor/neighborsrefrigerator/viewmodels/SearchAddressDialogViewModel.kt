package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.APiObject
import com.neighbor.neighborsrefrigerator.data.AddressDetail
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Response

class SearchAddressDialogViewModel:ViewModel() {

    var userAddressInput by mutableStateOf("")

    var addressList = MutableStateFlow<List<String>?>(null)

    fun setAddress(textField: String){
        val text: String = textField.let{
            it
        }
        val call = APiObject.retrofitService.getAddress(searchKeyword = text)

        var address: List<AddressDetail.HtReturnValue.Result.Zipcode> = arrayListOf()

        call.enqueue(object : retrofit2.Callback<AddressDetail> {
            override fun onResponse(call: Call<AddressDetail>, response: Response<AddressDetail>) {
                if (response.isSuccessful) {
                    try {
                        var _addressList: List<AddressDetail.HtReturnValue.Result.Zipcode> = arrayListOf()
                        _addressList = response.body()!!.htReturnValue.result.zipcodes

                        addressList.value = sendAddress(_addressList)
                        Log.d("성공", "${response.raw()}")
                        Log.d("리스트", addressList.value.toString())

                    } catch (e: NullPointerException) {
                        Log.d("실패1", e.message.toString())
                        val failedText = "검색 결과가 없습니다"
                        addressList.value = listOf(failedText)
                    }
                } else {
                    Log.d("실패2", "에러")
                }
            }
            override fun onFailure(call: Call<AddressDetail>, t: Throwable) {
                Log.d("실패3", t.message.toString())
            }
        })
    }

    fun sendAddress(addressDetail: List<AddressDetail.HtReturnValue.Result.Zipcode>): List<String>{

        val generalAddressList: ArrayList<String> = arrayListOf()
        var roadNameAddressList: ArrayList<String> = arrayListOf()
        // 도로명 주소가 추후에 필요하면 추가
        for (m in addressDetail) {
            generalAddressList.add(m.addressByNumberOfLot)
        }
        Log.d("성공", "주소 dialog에 전송")

        return generalAddressList
    }
}