package com.example.githubusersearch.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusersearch.di.Injection
import com.example.githubusersearch.domain.UserRepository
import com.example.githubusersearch.ui.favorite.FavoriteViewModel
import com.example.githubusersearch.ui.userdetail.UserDetailViewModel
import com.example.githubusersearch.ui.userlist.MainViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository, private val preferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideRepository(context),
                SettingPreferences.getInstance(context.dataStore)
            )
        }.also { instance = it }
    }
}