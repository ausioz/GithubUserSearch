package com.example.githubusersearch.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoriteUserEntity)

    @Delete
    fun deleteFavorite(favorite: FavoriteUserEntity)

    @Query("SELECT * FROM favorite WHERE login = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUserEntity>

    @Query("SELECT * FROM favorite")
    fun getFavoriteList(): LiveData<List<FavoriteUserEntity>>

}