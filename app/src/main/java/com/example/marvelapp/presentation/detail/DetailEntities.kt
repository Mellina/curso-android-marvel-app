package com.example.marvelapp.presentation.detail

import androidx.annotation.StringRes

data class DetailChildViewEntities(
    val id: Int,
    val imageUrl: String
)

data class DetailParentViewEntities(
    @StringRes
    val categoryStringResId: Int,
    val detailChildList: List<DetailChildViewEntities> = listOf()
)