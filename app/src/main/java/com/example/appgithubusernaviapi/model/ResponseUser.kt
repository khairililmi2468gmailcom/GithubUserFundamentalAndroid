package com.example.appgithubusernaviapi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ResponseUser (
    val userName : String,
    val name : String,
    val avatar: Int,
    var location: String,
    var repository: Int,
    var follower: Int,
    var following: Int,
    var company: String,
    ): Parcelable