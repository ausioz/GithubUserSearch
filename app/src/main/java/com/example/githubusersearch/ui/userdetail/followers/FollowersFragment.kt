package com.example.githubusersearch.ui.userdetail.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.databinding.FragmentFollowersBinding
import com.example.githubusersearch.ui.ViewModelFactory
import com.example.githubusersearch.ui.userdetail.UserDetailPagerAdapter
import com.example.githubusersearch.ui.userdetail.UserDetailViewModel


class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var factory: ViewModelFactory
    private val userDetailViewModel: UserDetailViewModel by viewModels { factory }
    private lateinit var followersAdapter: FollowersAdapter
    private var loginUser = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireActivity())

        arguments?.let {
            loginUser = it.getString(UserDetailPagerAdapter.STRING_USER).toString()
        }

        userDetailViewModel.listUserFollowers(loginUser)

        userDetailViewModel.userFollowers.observe(viewLifecycleOwner) {
            setData(it)
        }

        userDetailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        userDetailViewModel.errorMsg.observe(viewLifecycleOwner) {
            showError(it)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.recyclerViewFollowers.addItemDecoration(itemDecoration)

    }

    private fun setData(list: List<ItemsItem>) {
        followersAdapter = FollowersAdapter()
        followersAdapter.submitList(list)
        binding.recyclerViewFollowers.adapter = followersAdapter
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(requireContext(), "Error! \n$errorMsg", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerViewFollowers.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewFollowers.visibility = View.VISIBLE
        }
    }

}