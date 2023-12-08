package com.example.githubusersearch.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.R
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity
import com.example.githubusersearch.databinding.ActivityFavoriteBinding
import com.example.githubusersearch.ui.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var factory: ViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeActionContentDescription(getString(R.string.tomainactivity))

        factory = ViewModelFactory.getInstance(this)

        favoriteViewModel.getFavoriteList().observe(this) {
            setData(it)
            if (it.isNullOrEmpty()) binding.tvEmptyFavorite.visibility = View.VISIBLE
            else binding.tvEmptyFavorite.visibility = View.GONE
        }

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

    }

    private fun setData(list: List<FavoriteUserEntity>) {
        favoriteAdapter = FavoriteAdapter(this, onClickDelete = favoriteViewModel::deleteFavorite)
        favoriteAdapter.submitList(list)
        binding.recyclerViewFavorite.adapter = favoriteAdapter

    }
}