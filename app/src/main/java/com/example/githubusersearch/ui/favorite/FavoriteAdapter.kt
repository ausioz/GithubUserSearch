package com.example.githubusersearch.ui.favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity
import com.example.githubusersearch.databinding.ItemFavoriteBinding
import com.example.githubusersearch.ui.userdetail.UserDetailActivity
import com.example.githubusersearch.ui.userlist.UserAdapter

class FavoriteAdapter(
    private val context: Context, private val onClickDelete: (FavoriteUserEntity) -> Unit
) : ListAdapter<FavoriteUserEntity, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(
        private val binding: ItemFavoriteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteUserEntity) {
            binding.tvUser.text = favorite.login
            Glide.with(binding.root.context).load(favorite.avatarUrl).into(binding.ivUser)

            binding.cardFavorite.setOnClickListener {
                setListeners(getItem(bindingAdapterPosition), holder = ViewHolder(binding))
            }
            binding.imgDelete.setOnClickListener {
                onClickDelete(favorite)
                Toast.makeText(context, "Favorite telah dihapus", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun setListeners(selectedUser: FavoriteUserEntity, holder: FavoriteAdapter.ViewHolder) {
        val intent = Intent(holder.itemView.context, UserDetailActivity::class.java)
        // pass record to next activity
        intent.putExtra(UserDetailActivity.EXTRA_CATEGORY_DETAIL, UserDetailActivity.FROM_FAVORITE)
        intent.putExtra(UserAdapter.EXTRA_USER, selectedUser)
        holder.itemView.context.startActivity(intent)
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUserEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity
            ): Boolean {
                return oldItem == newItem
            }

            @Suppress("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}