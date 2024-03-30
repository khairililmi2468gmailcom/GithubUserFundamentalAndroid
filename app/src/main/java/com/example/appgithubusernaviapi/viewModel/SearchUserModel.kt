package com.example.appgithubusernaviapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appgithubusernaviapi.api.ApiConfig
import com.example.appgithubusernaviapi.model.Items
import com.example.appgithubusernaviapi.model.ResponseSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserModel : ViewModel() {

    companion object {
        private const val TAG = "SearchUserModel"
    }

    private val listSearch = MutableLiveData<ArrayList<Items>>()
    val listGetSearch : LiveData<ArrayList<Items>> = listSearch

    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingTake : MutableLiveData<Boolean> = isLoading

    fun searchUser(usrname: String){
        try{
            isLoading.value = true
            val clien = ApiConfig.getApiService().search(usrname)
            clien.enqueue(object: Callback<ResponseSearch>{
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    isLoading.value = false
                    val respon = response.body()
                    if(response.isSuccessful && respon != null){
                        listSearch.value=ArrayList(respon.items)
                    }
                }
                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                    isLoading.value = false
                    Log.e(SearchUserModel.TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
        catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }
}
