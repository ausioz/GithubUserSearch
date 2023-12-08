package com.example.githubusersearch.ui.userdetail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.FragmentFollowingBinding
import com.example.githubusersearch.ui.ViewModelFactory
import com.example.githubusersearch.ui.userdetail.UserDetailPagerAdapter
import com.example.githubusersearch.ui.userdetail.UserDetailViewModel


class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var factory: ViewModelFactory
    private val userDetailViewModel: UserDetailViewModel by viewModels { factory }
    private lateinit var followingAdapter: FollowingAdapter
    private var loginUser = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireActivity())

        arguments?.let {
            loginUser = it.getString(UserDetailPagerAdapter.STRING_USER).toString()
        }

        userDetailViewModel.listUserFollowing(loginUser)

        userDetailViewModel.userFollowing.observe(viewLifecycleOwner) {
            setData(it)
        }

        userDetailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        userDetailViewModel.errorMsg.observe(viewLifecycleOwner) {
            showError(it)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFollowing.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.recyclerViewFollowing.addItemDecoration(itemDecoration)

    }

    private fun setData(list: List<ItemsItem>) {
        followingAdapter = FollowingAdapter()
        followingAdapter.submitList(list)
        binding.recyclerViewFollowing.adapter = followingAdapter
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(requireContext(), "Error! \n$errorMsg", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerViewFollowing.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewFollowing.visibility = View.VISIBLE
        }
    }

}