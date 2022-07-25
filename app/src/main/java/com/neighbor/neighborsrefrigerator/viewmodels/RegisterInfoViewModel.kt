package com.neighbor.neighborsrefrigerator.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.APiObject
import com.neighbor.neighborsrefrigerator.data.AddressDetail
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Response

// textfield에 들어와 있는 데이터 저장
// nickname의 경우 변화가 감지될 경우 뷰모델에서 사용할 수 있는지 확인 후 true false 반환해 아이콘 색 변경해주기
// address의 경우 dialog에서 선택한 address를 클릭 시 textfield의 address에 자동으로 넣기
// dialog에서 address 검색 구성하기

class RegisterInfoViewModel : ViewModel() {
    private val _nickName = MutableLiveData<String>()
    //val addressList = MutableLiveData<List<String>>()
    var addressList = MutableStateFlow<List<String>?>(null)
    private val _dialogAddress = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickName


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

    private fun setClickList() {
        // dialog false
        // dialog에서 선택한 주소값 설정 주소로 띄우기
    }

    fun sendAddress(addressDetail: List<AddressDetail.HtReturnValue.Result.Zipcode>): List<String>{

        var generalAddressList: ArrayList<String> = arrayListOf()
        var roadNameAddressList: ArrayList<String> = arrayListOf()
        // 도로명 주소가 추후에 필요하면 추가
        for (m in addressDetail) {
            generalAddressList.add(m.addressByNumberOfLot)
        }
        Log.d("성공", "주소 dialog에 전송")

        return generalAddressList
    }
}
