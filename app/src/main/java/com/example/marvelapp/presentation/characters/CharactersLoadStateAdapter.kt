package com.example.marvelapp.presentation.characters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent

class CharactersLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharactersLoadMoreStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = CharactersLoadMoreStateViewHolder.create(parent, retry)

    override fun onBindViewHolder(holder: CharactersLoadMoreStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

}