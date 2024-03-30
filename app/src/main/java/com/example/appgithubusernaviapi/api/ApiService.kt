package com.example.appgithubusernaviapi.api
import com.example.appgithubusernaviapi.BuildConfig
import com.example.appgithubusernaviapi.model.ResponseSearch
import com.example.appgithubusernaviapi.model.ResponseFollowes
import com.example.appgithubusernaviapi.model.ResponseDetailUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: : Token ${BuildConfig.TOKEN}")
    fun search(
        @Query("q") username: String
    ): Call<ResponseSearch>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUsers>

    @GET("users/{username}/followers")
    fun followers(
        @Path("username") username: String
    ): Call<ArrayList<ResponseFollowes>>

    @GET("users/{username}/following")
    fun following(
        @Path("username") username: String
    ): Call<ArrayList<ResponseFollowes>>
}