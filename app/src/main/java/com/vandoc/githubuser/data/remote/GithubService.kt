package com.vandoc.githubuser.data.remote

import com.vandoc.githubuser.data.remote.response.UserMinimalResponse
import com.vandoc.githubuser.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<UserMinimalResponse>>

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserResponse>
}