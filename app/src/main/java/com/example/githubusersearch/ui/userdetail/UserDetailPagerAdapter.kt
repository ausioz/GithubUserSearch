package com.example.githubusersearch.ui.userdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusersearch.ui.userdetail.followers.FollowersFragment
import com.example.githubusersearch.ui.userdetail.following.FollowingFragment

class UserDetailPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var loginUser: String? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        val followersFragment = FollowersFragment()
        val followingFragment = FollowingFragment()
        followersFragment.arguments = Bundle().apply {
            putString(STRING_USER, loginUser)
        }
        followingFragment.arguments = Bundle().apply {
            putString(STRING_USER, loginUser)
        }

        when (position) {
            0 -> fragment = followersFragment
            1 -> fragment = followingFragment
        }
        return fragment as Fragment
    }

    companion object {
        const val STRING_USER = "user"
    }
}