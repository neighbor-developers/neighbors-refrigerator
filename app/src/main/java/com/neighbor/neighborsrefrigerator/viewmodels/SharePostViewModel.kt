package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.ProductData
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class SharePostViewModel : ViewModel() {
    val posts = MutableStateFlow<List<PostData>?>(null)
    val products = MutableStateFlow<List<ProductIncludeDistanceData>?>(
        listOf(
            ProductIncludeDistanceData(
            ProductData(",", 1, "감자", 1, Date(2022, 3, 4), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://avatars.githubusercontent.com/u/72335632?s=64&v=4"), 3.4),
            ProductIncludeDistanceData(
            ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://avatars.githubusercontent.com/u/72335632?s=64&v=4"), 5.7),
            ProductIncludeDistanceData(
            ProductData(",", 1, "대파", 3, Date(2022, 6, 9), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://www.nongmin.com/upload/old/www_data/photo/2017/0203/20170202145824.jpg"), 2.1),
            ProductIncludeDistanceData(
            ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://avatars.githubusercontent.com/u/72335632?s=64&v=4"), 3.3),

        )
    )

    fun search(content: String = "") {
        if (content != "") {
            /* 데이터 검색 */
        }
    }
    /*
    1. 데이터 가져오기
    2. 상품 시간순으로 노출
    3. 검색 했을때 데이터 가져오기
     - 나눔, 구함 구별
    4. 거리순
    * */
}
