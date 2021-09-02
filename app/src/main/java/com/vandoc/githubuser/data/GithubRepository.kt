package com.vandoc.githubuser.data

import com.vandoc.githubuser.data.remote.RemoteRepository
import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.model.User
import com.vandoc.githubuser.util.mapResource
import com.vandoc.githubuser.util.toModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val remoteRepository: RemoteRepository) :
    GithubDataSource {

    @OptIn(FlowPreview::class)
    override suspend fun getUsers(page: Int, perPage: Int): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.Loading)
            val response = remoteRepository.getUsers(page, perPage)
            emit(response)
        }.mapResource { response -> response.map { it.toModel() } }
    }
}