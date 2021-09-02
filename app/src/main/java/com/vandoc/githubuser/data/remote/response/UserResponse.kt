package com.vandoc.githubuser.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserMinimalResponse(
    val id: Int,
    @Json(name = "login") val username: String,
    @Json(name = "avatar_url") val avatar: String
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    val id: Int,
    @Json(name = "login") val username: String,
    @Json(name = "avatar_url") val avatar: String,
    val location: String,
    val email: String?,
    @Json(name = "created_at") val createdAt: String
)