package com.example.githubusersearch.data.remote.retrofit

import com.example.githubusersearch.BuildConfig
import com.example.githubusersearch.data.remote.response.DetailUserResponse
import com.example.githubusersearch.data.remote.response.GithubResponse
import com.example.githubusersearch.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object {
        private const val HEADERS = BuildConfig.GITHUB_TOKEN_HEADERS
    }

    @Headers(HEADERS)
    @GET("search/users")
    fun getGitUser(
        @Query("q") user: String
    ): Call<GithubResponse>

    @Headers(HEADERS)
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") login: String?
    ): Call<DetailUserResponse>

    @Headers(HEADERS)
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") login: String?
    ): Call<List<ItemsItem>>

    @Headers(HEADERS)
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") login: String?
    ): Call<List<ItemsItem>>
}

