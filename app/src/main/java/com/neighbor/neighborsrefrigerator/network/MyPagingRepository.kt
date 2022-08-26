package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.viewmodels.ReqPostData
import kotlinx.coroutines.flow.Flow

class MyPagingRepository {

    fun getPostsByTime(reqPostData: ReqPostData): Flow<PagingData<PostData>> {
        return Pager(config = PagingConfig(
            pageSize = 5,
            maxSize = 20,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { MyPagingSource(reqPostData) }
        ).flow
    }


}