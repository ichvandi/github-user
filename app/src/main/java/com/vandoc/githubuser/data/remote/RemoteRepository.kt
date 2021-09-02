package com.vandoc.githubuser.data.remote

import com.vandoc.githubuser.data.remote.response.User
import com.vandoc.githubuser.data.remote.response.UserMinimal
import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.util.Helper
import com.vandoc.githubuser.util.getError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(private val service: GithubService) {

    suspend fun getUsers(page: Int, perPage: Int): Resource<List<UserMinimal>> {
        return Helper.runCatching {
            val response = service.getUsers(page, perPage)
            if (response.isSuccessful) {
                response.body() ?: Error("An error occurred!")
            } else {
                response.getError()
            }
        }
    }

    suspend fun getUserDetail(username: String): Resource<User> {
        return Helper.runCatching {
            val response = service.getUserDetail(username)
            if (response.isSuccessful) {
                response.body() ?: Error("An error occurred!")
            } else {
                response.getError()
            }
        }
    }
}