package com.example.appgithubusernaviapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appgithubusernaviapi.api.ApiConfig
import com.example.appgithubusernaviapi.model.ResponseFollowes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingModel : ViewModel() {

    companion object {
        private const val TAG = "FollowingModel"
    }

    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingTake : MutableLiveData<Boolean> = isLoading

    private val following = MutableLiveData<ArrayList<ResponseFollowes>>()
    val listFollowing : LiveData<ArrayList<ResponseFollowes>> = following

    fun following(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().following(username)
            client.enqueue(object : Callback<ArrayList<ResponseFollowes>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseFollowes>>,
                    response: Response<ArrayList<ResponseFollowes>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        following.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponseFollowes>>, t: Throwable) {
                    isLoading.value = false
                    Log.e(FollowingModel.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }
}
