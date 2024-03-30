package com.example.appgithubusernaviapi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavUsersDao {
    @Insert
    fun addToFavor(fav_user: FavUsers?)

    @Query("SELECT * FROM fav_users")
    fun getFavUsers(): LiveData<List<FavUsers>>

    @Query("DELETE FROM fav_users WHERE fav_users.id = :id")
    fun removeFavUser(id: Int): Int

    @Query("SELECT count(*) FROM fav_users WHERE fav_users.id = :id")
    fun checkUsr(id: Int): Int
}