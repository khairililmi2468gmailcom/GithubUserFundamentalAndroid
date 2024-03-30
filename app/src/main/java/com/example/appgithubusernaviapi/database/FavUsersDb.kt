package com.example.appgithubusernaviapi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavUsers::class],
    version=1
)
abstract class FavUsersDb : RoomDatabase(){
    companion object{
        var INSTAN: FavUsersDb? = null
        fun getDb(context: Context):FavUsersDb?{
            if(INSTAN == null){
                synchronized(FavUsersDb::class){
                    INSTAN = Room.databaseBuilder(context.applicationContext, FavUsersDb::class.java,"users_db").build()
                }
            }
            return INSTAN
        }
    }
    abstract fun favUsersDao(): FavUsersDao
}