package com.example.githubusersearch.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity
import com.example.githubusersearch.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getFavoriteList() = userRepository.getFavoriteList()

    fun deleteFavorite(favorite: FavoriteUserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteFavorite(favorite)
        }
    }
}
