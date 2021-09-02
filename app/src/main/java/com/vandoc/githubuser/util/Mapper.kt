package com.vandoc.githubuser.util

import com.vandoc.githubuser.data.remote.response.UserResponse
import com.vandoc.githubuser.model.User

fun UserResponse.toModel() = User(
    id = id,
    username = username,
    avatar = avatar,
    location = location,
    email = email,
    createdAt = createdAt
)