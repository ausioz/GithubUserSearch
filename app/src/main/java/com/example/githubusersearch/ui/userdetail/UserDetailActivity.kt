package com.example.githubusersearch.ui.userdetail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusersearch.R
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.ActivityUserDetailBinding
import com.example.githubusersearch.ui.ViewModelFactory
import com.example.githubusersearch.ui.userlist.UserAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var factory: ViewModelFactory
    private val userDetailViewModel: UserDetailViewModel by viewModels { factory }
    private lateinit var category: String
    private lateinit var login: String
    private lateinit var avatar: String
    private var isAllFabsVisible: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        category = intent.getStringExtra(EXTRA_CATEGORY_DETAIL).orEmpty()

        if (category == FROM_MAIN) {
            val parcel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(UserAdapter.EXTRA_USER, ItemsItem::class.java)
            } else {
                @Suppress("DEPRECATION") intent.getParcelableExtra(UserAdapter.EXTRA_USER) as? ItemsItem
            }
            login = parcel?.login.toString()
            avatar = parcel?.avatarUrl.toString()
        }
        if (category == FROM_FAVORITE) {
            val parcel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    UserAdapter.EXTRA_USER, FavoriteUserEntity::class.java
                )
            } else {
                @Suppress("DEPRECATION") intent.getParcelableExtra(UserAdapter.EXTRA_USER) as? FavoriteUserEntity
            }
            login = parcel?.login.toString()
            avatar = parcel?.avatarUrl.toString()
        }
        userDetailViewModel.detailUser(login)

        userDetailViewModel.userDetail.observe(this) {
            Glide.with(this).load(it.avatarUrl).centerCrop().into(binding.ivProfile)
            binding.tvLogin.text = it.login
            binding.tvName.text = it.name
            binding.tvFollowers.text = getString(R.string.followers, it.followers.toString())
            binding.tvFollowing.text = getString(R.string.following, it.following.toString())
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        userDetailViewModel.errorMsg.observe(this) {
            showError(it)
        }

        binding.fabFavorite.visibility = View.GONE
        binding.fabShare.visibility = View.GONE
        binding.tvShare.visibility = View.GONE
        binding.tvFavorite.visibility = View.GONE
        isAllFabsVisible = false

        binding.addFab.shrink()



        userDetailViewModel.getFavoriteUserByUsername(login).observe(this) { username ->

            binding.addFab.setOnClickListener {
                isAllFabsVisible = if (!isAllFabsVisible!!) {
                    binding.fabFavorite.show()
                    binding.fabShare.show()
                    binding.tvShare.visibility = View.VISIBLE
                    binding.tvFavorite.visibility = View.VISIBLE
                    binding.addFab.extend()
                    true
                } else {
                    binding.fabFavorite.hide()
                    binding.fabShare.hide()
                    binding.tvShare.visibility = View.GONE
                    binding.tvFavorite.visibility = View.GONE
                    binding.addFab.shrink()
                    false
                }
            }

            //favorite action
            if (username != null) {
                binding.fabFavorite.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.baseline_favorite_24_fill, null
                    )
                )
                binding.tvFavorite.text = getString(R.string.remove_from_favorite)
                binding.fabFavorite.setOnClickListener {
                    userDetailViewModel.deleteFavorite(username)
                    Toast.makeText(this, "Favorite dibatalkan", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.fabFavorite.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.baseline_favorite_border_24, null
                    )
                )
                binding.tvFavorite.text = getString(R.string.add_to_favorite)
                binding.fabFavorite.setOnClickListener {
                    userDetailViewModel.insertFavorite(
                        FavoriteUserEntity(
                            login, avatar
                        )
                    )
                    Toast.makeText(this, "Favorite ditambahkan", Toast.LENGTH_SHORT).show()
                }
            }

            //share action
            binding.fabShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/$login")
                startActivity(Intent.createChooser(shareIntent, "Share profile link"))
            }

        }


        val pagerAdapter = UserDetailPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        pagerAdapter.loginUser = login
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(this, "Error! \n$errorMsg", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                progressBar.visibility = View.VISIBLE
                ivProfile.visibility = View.GONE
                tvLogin.visibility = View.GONE
                tvName.visibility = View.GONE
                tvFollowers.visibility = View.GONE
                tvFollowing.visibility = View.GONE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
            }

        } else {
            binding.apply {
                progressBar.visibility = View.GONE
                ivProfile.visibility = View.VISIBLE
                tvLogin.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
                tvFollowers.visibility = View.VISIBLE
                tvFollowing.visibility = View.VISIBLE
                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
            }

        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_title, R.string.following_title
        )

        const val EXTRA_CATEGORY_DETAIL = "category"
        const val FROM_MAIN = "main"
        const val FROM_FAVORITE = "favorite"
    }
}