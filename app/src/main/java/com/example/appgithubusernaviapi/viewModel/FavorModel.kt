package com.example.appgithubusernaviapi.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.appgithubusernaviapi.database.FavUsers
import com.example.appgithubusernaviapi.database.FavUsersDao
import com.example.appgithubusernaviapi.database.FavUsersDb

class FavorModel(application: Application) : AndroidViewModel(application) {
    private var usrDao: FavUsersDao? = null

    init {
        val db = FavUsersDb.getDb(application)
        usrDao = db?.favUsersDao()
    }
    fun getFavorUsers(): LiveData<List<FavUsers>>? {
        val favorUsersLiveData = usrDao?.getFavUsers()

        // Tambahkan log untuk memeriksa hasil dari pemanggilan fungsi ini
        favorUsersLiveData?.observeForever { favorUsersList ->
            Log.d("FavorModel", "Jumlah data favorit: ${favorUsersList.size}")
            for (favorUser in favorUsersList) {
                Log.d("FavorModel", "ID: ${favorUser.id}, Login: ${favorUser.login}, Avatar URL: ${favorUser.avatarUrl}")
            }
        }

        return favorUsersLiveData
    }

}