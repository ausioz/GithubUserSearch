package com.example.githubusersearch.ui.userdetail

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubusersearch.data.local.entity.FavoriteUserEntity
import com.example.githubusersearch.data.local.room.UserDao
import com.example.githubusersearch.data.local.room.UserDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    private lateinit var userDatabase: UserDatabase
    private lateinit var userDao: UserDao

    private val favoriteDummy =
        FavoriteUserEntity("tes", "\"https://avatars.githubusercontent.com/u/7055819?v=4\"")

    @Before
    fun setUpDb() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        userDatabase = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = userDatabase.userDao()
    }

    @After
    fun closeDb() {
        userDatabase.close()
    }

    @Test
    fun insertFavorite() = runBlocking {
        userDao.insertFavorite(favoriteDummy)
        val favoriteList = userDao.getFavoriteList().value
        if (favoriteList != null) {
            assert(favoriteList.contains(favoriteDummy))
        }
    }

    @Test
    fun deleteFavorite() {
        userDao.insertFavorite(favoriteDummy)
        userDao.deleteFavorite(favoriteDummy)
        val favoriteList = userDao.getFavoriteList().value
        if (favoriteList != null) {
            assertNotNull(favoriteList.contains(favoriteDummy))
        }
    }

    @Test
    fun getUserByName() {
        userDao.insertFavorite(favoriteDummy)
        userDao.getFavoriteUserByUsername("tes")
        val favoriteList = userDao.getFavoriteList().value
        if (favoriteList != null) {
            assert(favoriteList.contains(favoriteDummy))
        }
    }

}