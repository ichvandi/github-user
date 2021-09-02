package com.vandoc.githubuser.data

import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.model.User
import com.vandoc.githubuser.model.UserMinimal
import kotlinx.coroutines.flow.Flow

interface GithubDataSource {

    suspend fun getUsers(page: Int, perPage: Int): Flow<Resource<List<UserMinimal>>>

    suspend fun getUserDetail(username: String): Flow<Resource<User>>
}