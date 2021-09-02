package com.vandoc.githubuser.util

import com.vandoc.githubuser.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Helper {
    inline fun <reified T> runCatching(block: () -> Any): Resource<T> {
        return try {
            return when (val response = block()) {
                is Error -> Resource.Error(response.message ?: "")
                is T -> Resource.Success(response)
                else -> throw IllegalArgumentException("Return value can only be T or Error!")
            }
        } catch (e: SocketTimeoutException) {
            Resource.Error("The connection has timed out")
        } catch (e: Exception) {
            when (e) {
                is ConnectException, is UnknownHostException -> Resource.Error("Unable to connect to server, please check your internet connection")
                else -> throw e
            }
        } catch (e: Exception) {
            Resource.Error("Unknown error occurred")
        }
    }
}

fun Response<*>.getError(): Error {
    val error = errorBody()?.charStream()?.readText() ?: "Unknown Error: Can't parse error message"
    return Error(error)
}

inline fun <T, R> Flow<Resource<T>>.mapResource(crossinline mapper: (value: T) -> R): Flow<Resource<R>> =
    transform { value ->
        return@transform when (value) {
            is Resource.Success -> emit(Resource.Success(mapper(value.data)))
            is Resource.Error -> emit(Resource.Error(value.message))
            is Resource.Loading -> emit(Resource.Loading)
        }
    }
