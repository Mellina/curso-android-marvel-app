package com.example.marvelapp.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemParentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailParentAdapter(
    private val detailParentList: List<DetailParentViewEntities>,
    private val imageLoader: ImageLoader
): RecyclerView.Adapter<DetailParentAdapter.DetailParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailParentViewHolder {
        return DetailParentViewHolder.create(parent, imageLoader)
    }

    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        holder.bind(detailParentList[position])
    }

    override fun getItemCount() = detailParentList.size

    class DetailParentViewHolder(
        private val imageLoader: ImageLoader,
        itemBinding: ItemParentDetailBinding
    ): RecyclerView.ViewHolder(itemBinding.root) {

            private val textItemCategory = itemBinding.textItemCategory
            private val recyclerChildDetail = itemBinding.recyclerChildDetail

        fun bind(detailParentViewEntities: DetailParentViewEntities) {
           textItemCategory.text = itemView.context.getString(detailParentViewEntities.categoryStringResId)
           recyclerChildDetail.run{
               setHasFixedSize(true)
               adapter = DetailChildAdapter(detailParentViewEntities.detailChildList, imageLoader)
           }
        }

        companion object {
            fun create(parent: ViewGroup,
                       imageLoader: ImageLoader): DetailParentViewHolder {
                val itemBinding = ItemParentDetailBinding
                    .inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)

                return DetailParentViewHolder(imageLoader, itemBinding)
            }
        }
    }
}