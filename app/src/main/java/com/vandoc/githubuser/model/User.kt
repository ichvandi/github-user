package com.vandoc.githubuser.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserMinimal(
    val id: Int,
    val username: String,
    val avatar: String
)

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val username: String,
    val avatar: String,
    val location: String?,
    val email: String?,
    val createdAt: String
)