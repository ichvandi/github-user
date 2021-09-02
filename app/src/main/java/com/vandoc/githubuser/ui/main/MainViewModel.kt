package com.vandoc.githubuser.ui.main

import androidx.lifecycle.viewModelScope
import com.vandoc.githubuser.base.BaseViewModel
import com.vandoc.githubuser.data.GithubRepository
import com.vandoc.githubuser.data.util.Resource
import com.vandoc.githubuser.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: GithubRepository) :
    BaseViewModel() {
    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>> = _users

    private var since = 0
    private var isLoading = false
    private var isEmpty = false

    companion object {
        private const val PER_PAGE = 10
    }

    init {
        getUsers(since)
    }

    fun getUsers(page: Int? = null) {
        if (isLoading && isEmpty) return

        viewModelScope.launch {
            isLoading = true
            val oldUsers = _users.value

            repository.getUsers(page ?: since, PER_PAGE).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val data = response.data
                        if (data.isEmpty()) {
                            isEmpty = true
                            return@collect
                        }

                        since = response.data.last().id

                        _uiState.send(UiState.Success())
                        _users.value = if (page == 0) response.data else oldUsers + response.data
                    }
                    is Resource.Error -> _uiState.send(UiState.Error(response.message))
                    is Resource.Loading -> _uiState.send(UiState.Loading)
                }

                isLoading = false
            }
        }
    }
}