package com.example.marvelapp.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemChildDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailChildAdapter(
    private val detailChildList: List<DetailChildViewEntities>,
    private val imageLoader: ImageLoader
): RecyclerView.Adapter<DetailChildAdapter.DetailChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailChildViewHolder {
        return DetailChildViewHolder.create(parent, imageLoader)
    }

    override fun onBindViewHolder(holder: DetailChildViewHolder, position: Int) {
        holder.bind(detailChildList[position])
    }

    override fun getItemCount() = detailChildList.size

    class DetailChildViewHolder(
        private val imageLoader: ImageLoader,
        itemBinding: ItemChildDetailBinding
    ): RecyclerView.ViewHolder(itemBinding.root) {

        private val imageCategory =itemBinding.imageItemCategory

        fun bind(detailChildViewEntities: DetailChildViewEntities) {
            imageLoader.load(imageCategory,
                detailChildViewEntities.imageUrl)
        }

        companion object {
            fun create(parent: ViewGroup,
                       imageLoader: ImageLoader): DetailChildViewHolder {
                val itemBinding = ItemChildDetailBinding
                    .inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)

                return DetailChildViewHolder(imageLoader, itemBinding)
            }
        }
    }
}