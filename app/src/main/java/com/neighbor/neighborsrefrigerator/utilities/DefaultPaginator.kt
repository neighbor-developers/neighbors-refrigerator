package com.neighbor.neighborsrefrigerator.utilities

import androidx.paging.PagingSource

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated:(Boolean) -> Unit,

){
}