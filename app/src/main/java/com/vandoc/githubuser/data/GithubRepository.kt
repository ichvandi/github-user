package com.vandoc.githubuser.data

import com.vandoc.githubuser.data.remote.RemoteRepository
import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.model.User
import com.vandoc.githubuser.util.toMap
import com.vandoc.githubuser.util.toModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
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

            when (val response = remoteRepository.getUsers(page, perPage)) {
                is Resource.Success -> {
                    val responseDetail = response.data.asFlow().flatMapMerge {
                        flow {
                            emit(it.username to remoteRepository.getUserDetail(it.username))
                        }
                    }.toMap()

                    val usernames = response.data.map { it.username }
                    val users = usernames.map { (responseDetail[it]!! as Resource.Success).data }

                    emit(Resource.Success(users.map { it.toModel() }))
                }
                is Resource.Error -> emit(response)
                Resource.Loading -> emit(Resource.Loading)
            }
        }
    }
}