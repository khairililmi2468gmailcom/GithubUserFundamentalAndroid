package com.example.appgithubusernaviapi.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="fav_users")
data class FavUsers(
    @PrimaryKey val id : Int,
    val login: String,
    val avatarUrl: String
):Serializable
