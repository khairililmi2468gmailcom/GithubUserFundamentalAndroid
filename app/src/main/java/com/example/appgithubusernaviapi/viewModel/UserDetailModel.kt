package com.example.appgithubusernaviapi.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appgithubusernaviapi.api.ApiConfig
import com.example.appgithubusernaviapi.database.FavUsers
import com.example.appgithubusernaviapi.database.FavUsersDao
import com.example.appgithubusernaviapi.database.FavUsersDb
import com.example.appgithubusernaviapi.model.ResponseDetailUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "UserDetailModel"
    }
    private var usrDao : FavUsersDao?
    private var usrDb : FavUsersDb?

    init{
        usrDb = FavUsersDb.getDb(application)
        usrDao = usrDb?.favUsersDao()
    }

    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingTake: MutableLiveData<Boolean> = isLoading

    private val detailUsers = MutableLiveData<ResponseDetailUsers>()
    val listDetailUsers: LiveData<ResponseDetailUsers> = detailUsers

    fun detailUser(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().detailUser(username)
            client.enqueue(object : Callback<ResponseDetailUsers> {
                override fun onResponse(
                    call: Call<ResponseDetailUsers>,
                    response: Response<ResponseDetailUsers>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        detailUsers.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ResponseDetailUsers>, t: Throwable) {
                    isLoading.value = false
                    Log.e(UserDetailModel.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }
    fun addToFavorite(id: Int, username: String, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavUsers(id, username, avatarUrl )
            usrDao?.addToFavor(user)
        }
    }

    suspend fun checkUsr(id:Int) = usrDao?.checkUsr(id)
    fun removeFavor(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            usrDao?.removeFavUser(id)
        }
    }
}
