package com.example.githubusersearch.ui.userdetail.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.ItemFollowingBinding

class FollowingAdapter : ListAdapter<ItemsItem, FollowingAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemFollowingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(follower: ItemsItem) {
            Glide.with(binding.root.context).load(follower.avatarUrl).centerCrop()
                .into(binding.ivUser)
            binding.tvUser.text = follower.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFollowingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(
                oldItem: ItemsItem, newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemsItem, newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}