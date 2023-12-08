package com.example.githubusersearch.domain

import androidx.lifecycle.LiveData
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity
import com.example.githubusersearch.data.local.room.UserDao
import com.example.githubusersearch.data.remote.response.DetailUserResponse
import com.example.githubusersearch.data.remote.response.GithubResponse
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.data.remote.retrofit.ApiConfig
import retrofit2.Call

class UserRepository private constructor(
    private val apiConfig: ApiConfig,
    private val userDao: UserDao,
) {
    //remote
    fun getGitUser(user: String): Call<GithubResponse> {
        return apiConfig.getApiService().getGitUser(user)
    }

    fun getDetailUser(user: String): Call<DetailUserResponse> {
        return apiConfig.getApiService().getDetailUser(user)
    }

    fun getFollowers(user: String): Call<List<ItemsItem>> {
        return apiConfig.getApiService().getFollowers(user)
    }

    fun getFollowing(user: String): Call<List<ItemsItem>> {
        return apiConfig.getApiService().getFollowing(user)
    }


    fun insertFavorite(favorite: FavoriteUserEntity) {
        userDao.insertFavorite(favorite)
    }

    fun deleteFavorite(favorite: FavoriteUserEntity) {
        userDao.deleteFavorite(favorite)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUserEntity> {
        return userDao.getFavoriteUserByUsername(username)
    }

    fun getFavoriteList(): LiveData<List<FavoriteUserEntity>> {
        return userDao.getFavoriteList()
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiConfig: ApiConfig, newsDao: UserDao,
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiConfig, newsDao)
        }.also { instance = it }
    }
}