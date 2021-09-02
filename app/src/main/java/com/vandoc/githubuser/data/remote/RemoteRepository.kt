package com.vandoc.githubuser.data.remote

import com.vandoc.githubuser.data.remote.response.UserResponse
import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.util.Helper
import com.vandoc.githubuser.util.getError
import com.vandoc.githubuser.util.toMap
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(private val service: GithubService) {

    @OptIn(FlowPreview::class)
    suspend fun getUsers(page: Int, perPage: Int): Resource<List<UserResponse>> {
        return Helper.runCatching {
            val response = service.getUsers(page, perPage)
            if (response.isSuccessful) {
                val usersMin = response.body() ?: return@runCatching Error("An error occurred!")
                val responseDetail = usersMin.asFlow().flatMapMerge {
                    flow {
                        emit(it.username to service.getUserDetail(it.username))
                    }
                }.toMap()

                val usernames = usersMin.map { it.username }
                val data = usernames.map { responseDetail[it]!! }
                val users = data.filter { it.isSuccessful }.map { it.body()!! }
                val errors = data.filter { !it.isSuccessful }.map { it.getError() }

                if (errors.isNotEmpty()) errors else users
            } else {
                response.getError()
            }
        }
    }

}