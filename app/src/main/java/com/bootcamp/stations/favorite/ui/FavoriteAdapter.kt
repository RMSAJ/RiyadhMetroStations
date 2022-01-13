package com.bootcamp.stations.favorite.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.stations.databinding.FavItemBinding
import com.bumptech.glide.Glide


class FavoriteAdapter (): ListAdapter<FavoriteUiState, FavoriteAdapter.ResultsItemViewHolder>(DiffCallback) {

    class ResultsItemViewHolder(var binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ItemOfFav : FavoriteUiState ) {
            binding.favorItem = ItemOfFav
            binding.title.text = ItemOfFav.title
            binding.secondary.text = ItemOfFav.description
            binding.contentDescription.text = ItemOfFav.subTitle
            Glide.with(binding.stationImage).load(ItemOfFav.image).into(binding.stationImage)
            binding.executePendingBindings()
        }

    }
    companion object DiffCallback : DiffUtil.ItemCallback<FavoriteUiState>() {
        override fun areItemsTheSame(oldItem: FavoriteUiState, newItem: FavoriteUiState): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: FavoriteUiState, newItem: FavoriteUiState): Boolean {
            return oldItem.title == newItem.title
        }
    } // end DiffCallback Object

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsItemViewHolder {
        return ResultsItemViewHolder(FavItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ResultsItemViewHolder, position: Int) {

        val listProject = getItem(position)
        holder.bind(listProject)
    } // end



} // end ItemListAdapter








