package com.neighbor.neighborsrefrigerator.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.viewmodels.ReqPostData
import kotlinx.coroutines.flow.SharedFlow
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class MyPagingSource(
    private val reqPostData: ReqPostData
): PagingSource<Int, PostData>() {
    private val dbAccessModule = DBAccessModule()

    // 스크롤시 데이터를 비동기식으로 가져오기 위한 함수
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostData> {
        // LoadParams: 로드할 키와 항목 수, LoadResult: 로드 작업의 결과
        try {
            val position = params.key?: STARTING_PAGE_INDEX
            val result = dbAccessModule.getPostOrderByTime(page = position,reqPostData = reqPostData)
//            Log.d("결과2", result.toString())
            return if (result != emptyList<PostData>()){
                LoadResult.Page(
                    data = result,
                    prevKey = when(position){
                        STARTING_PAGE_INDEX -> null
                        else -> position -1
                    },
                    nextKey = position + 1
                )
            }else{
                LoadResult.Invalid()

            }

            // PagingSource가 더 이상 결과의 무결성을 보장할 수 없으므로 무효화되어야 하는 경우
//            LoadResult.Invalid()
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }


    }
    override fun getRefreshKey(state: PagingState<Int, PostData>): Int? {
        return state.anchorPosition
    }

}