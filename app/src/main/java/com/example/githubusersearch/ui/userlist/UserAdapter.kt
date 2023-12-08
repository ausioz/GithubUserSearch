package com.example.githubusersearch.ui.userlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.ItemUserBinding
import com.example.githubusersearch.ui.userdetail.UserDetailActivity

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvUser.text = user.login
            Glide.with(binding.root.context).load(user.avatarUrl).into(binding.ivUser)

            binding.cardUser.setOnClickListener {
                setListeners(getItem(bindingAdapterPosition), holder = ViewHolder(binding))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun setListeners(selectedUser: ItemsItem, holder: ViewHolder) {
        val intent = Intent(holder.itemView.context, UserDetailActivity::class.java)
        // pass record to next activity
        intent.putExtra(UserDetailActivity.EXTRA_CATEGORY_DETAIL, UserDetailActivity.FROM_MAIN)
        intent.putExtra(EXTRA_USER, selectedUser)
        holder.itemView.context.startActivity(intent)
    }

    companion object {
        const val EXTRA_USER = "user"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
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
