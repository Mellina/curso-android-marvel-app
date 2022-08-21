package com.example.marvelapp.presentation.common

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class GenericViewHolder<T>(
    itemBinging: ViewBinding
): RecyclerView.ViewHolder(itemBinging.root) {

    abstract fun bind(data: T)
}