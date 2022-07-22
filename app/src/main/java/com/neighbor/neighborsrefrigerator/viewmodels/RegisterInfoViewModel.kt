package com.neighbor.neighborsrefrigerator.viewmodels

import android.content.ClipData
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.APiObject
import com.neighbor.neighborsrefrigerator.data.AddressDetail
import retrofit2.Call
import retrofit2.Response

// textfield에 들어와 있는 데이터 저장
// nickname의 경우 변화가 감지될 경우 뷰모델에서 사용할 수 있는지 확인 후 true false 반환해 아이콘 색 변경해주기
// address의 경우 dialog에서 선택한 address를 클릭 시 textfield의 address에 자동으로 넣기
// dialog에서 address 검색 구성하기

class RegisterInfoViewModel : ViewModel() {
    private val _nickName = MutableLiveData<String>()
    private val _address = MutableLiveData<String>()
    private val _dialogAddress = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickName


    fun setAddress(textField: String, completion: (List<AddressDetail.HtReturnValue.Result.Zipcode>)->Unit){
        val text: String = textField.let{
            it
        }
        val call = APiObject.retrofitService.getAddress(searchKeyword = text)

        var address: List<AddressDetail.HtReturnValue.Result.Zipcode> = arrayListOf()

        call.enqueue(object : retrofit2.Callback<AddressDetail> {
            override fun onResponse(call: Call<AddressDetail>, response: Response<AddressDetail>) {
                if (response.isSuccessful) {
                    try {
                        var addressDetail: List<AddressDetail.HtReturnValue.Result.Zipcode> = arrayListOf()
                        addressDetail = response.body()!!.htReturnValue.result.zipcodes

                        Log.d("성공", "${response.raw()}")
                        completion(addressDetail)
                    } catch (e: NullPointerException) {
                        Log.d("실패", e.message.toString())
                    }
                } else {
                    Log.d("실패", "에러")
                }
            }
            override fun onFailure(call: Call<AddressDetail>, t: Throwable) {
                Log.d("실패", t.message.toString())
            }
        })
        Log.d("확인", "주소 검색 작동")
    }

    private fun setClickList() {
        // dialog false
        // dialog에서 선택한 주소값 설정 주소로 띄우기
    }

    fun sendAddress(addressDetail: List<AddressDetail.HtReturnValue.Result.Zipcode>): List<String>{

        var generalAddressList: List<String> = arrayListOf()
        var roadNameAddressList: List<String> = arrayListOf()
        // 도로명 주소가 추후에 필요하면 추가

        for (m in addressDetail) {
            generalAddressList = generalAddressList.plus(m.addressByNumberOfLot)
        }
        Log.d("성공", "주소 dialog에 전송")

        return generalAddressList
    }
}
