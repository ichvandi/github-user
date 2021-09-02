package com.vandoc.githubuser.util

import com.vandoc.githubuser.data.remote.response.UserMinimalResponse
import com.vandoc.githubuser.data.remote.response.UserResponse
import com.vandoc.githubuser.model.User
import com.vandoc.githubuser.model.UserMinimal

fun UserMinimalResponse.toModel() = UserMinimal(
    id = id,
    username = username,
    avatar = avatar
)

fun UserResponse.toModel() = User(
    id = id,
    username = username,
    avatar = avatar,
    location = location,
    email = email,
    createdAt = createdAt
)