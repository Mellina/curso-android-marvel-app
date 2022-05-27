package com.bassul.testing.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.domain.model.Character

class PagingSourceFactory {

    fun create(heroes: List<Character>) = object : PagingSource<Int, com.example.core.domain.model.Character>() {
        override fun getRefreshKey(state: PagingState<Int, com.example.core.domain.model.Character>) = 1

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.example.core.domain.model.Character> {
            return LoadResult.Page(
                data = heroes,
                prevKey = null,
                nextKey = 20
            )
        }

    }


}