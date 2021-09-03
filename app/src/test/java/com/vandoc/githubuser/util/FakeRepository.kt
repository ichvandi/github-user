package com.vandoc.githubuser.util

import com.vandoc.githubuser.data.GithubDataSource
import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : GithubDataSource {

    override suspend fun getUsers(page: Int, perPage: Int): Flow<Resource<List<User>>> {
        return flow {
            emit(
                Resource.Success(
                    listOf(
                        FakeData.fakeUser
                    )
                )
            )
        }
    }
}