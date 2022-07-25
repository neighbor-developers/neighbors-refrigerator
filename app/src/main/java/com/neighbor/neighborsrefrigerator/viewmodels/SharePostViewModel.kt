package com.neighbor.neighborsrefrigerator.viewmodels

import androidx.lifecycle.ViewModel
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.ProductIncludeDistanceData
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*

class SharePostViewModel : ViewModel() {
//    - postID: Int
//    - title: String
//    - category: String - ex 100_10
//    - uid: Int
//    - content: String
//    - post_type: Int - 나눔, 구함 - 1, 2
//    - post_addr: String
//    - rate: Double
//    - review: String
//    - created_at: Date
//    - updated_at: Date
//    - completed_at: Date - not null이면 완료
    val posts = MutableStateFlow<List<PostData>?>(
    listOf())

    val products = MutableStateFlow<List<ProductIncludeDistanceData>?>(
        listOf(
            ProductIncludeDistanceData(
            ProductData(",", 1, "감자", 1, Date(2022, 3, 4), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://mediahub.seoul.go.kr/wp-content/uploads/2016/09/61a2981f41200ac8c513a3cbc0010efe.jpg"), 3.4),
            ProductIncludeDistanceData(
            ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg"), 5.7),
            ProductIncludeDistanceData(
            ProductData(",", 1, "대파", 3, Date(2022, 6, 9), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://www.nongmin.com/upload/old/www_data/photo/2017/0203/20170202145824.jpg"), 2.1),
            ProductIncludeDistanceData(
            ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg"), 3.3),
            ProductIncludeDistanceData(
                ProductData(",", 1, "감자", 1, Date(2022, 3, 4), "https://mediahub.seoul.go.kr/wp-content/uploads/2016/09/61a2981f41200ac8c513a3cbc0010efe.jpg", "https://cdn.mindgil.com/news/photo/202007/69482_3679_1449.jpg"), 3.4),
            ProductIncludeDistanceData(
                ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg"), 5.7),
            ProductIncludeDistanceData(
                ProductData(",", 1, "감자", 1, Date(2022, 3, 4), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://mediahub.seoul.go.kr/wp-content/uploads/2016/09/61a2981f41200ac8c513a3cbc0010efe.jpg"), 3.4),
            ProductIncludeDistanceData(
                ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg"), 5.7),
            ProductIncludeDistanceData(
                ProductData(",", 1, "대파", 3, Date(2022, 6, 9), "https://avatars.githubusercontent.com/u/72335632?s=64&v=4", "https://www.nongmin.com/upload/old/www_data/photo/2017/0203/20170202145824.jpg"), 2.1),
            ProductIncludeDistanceData(
                ProductData(",", 1, "고구마", 2, Date(2022, 3, 4), null, "https://src.hidoc.co.kr/image/lib/2022/4/13/1649807075785_0.jpg"), 3.3)
            )
    )
    fun search(item: String, type : String) {
        if (item != "") {
            when(type) {
                "share" -> {}
                "seek" -> {}
            }
        }
    }
    fun chooseCategory(item: String){
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Date(System.currentTimeMillis()))
        val curTime = dateFormat.format(Date(time))
    }
    /*
    1. 데이터 가져오기
    2. 상품 시간순으로 노출
    3. 검색 했을때 데이터 가져오기
     - 나눔, 구함 구별
    4. 거리순
    */
}
