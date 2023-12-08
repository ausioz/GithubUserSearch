package com.example.githubusersearch.di

import android.content.Context
import com.example.githubusersearch.data.local.room.UserDatabase
import com.example.githubusersearch.data.remote.retrofit.ApiConfig
import com.example.githubusersearch.domain.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiConfig = ApiConfig()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()

        return UserRepository.getInstance(apiConfig, dao)
    }
}