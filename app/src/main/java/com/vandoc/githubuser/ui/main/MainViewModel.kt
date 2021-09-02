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

    init {
        getUsers(1, 10)
    }

    private fun getUsers(page: Int, perPage: Int) {
        viewModelScope.launch {
            repository.getUsers(page, perPage).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _uiState.send(UiState.Success())
                        _users.value = response.data
                    }
                    is Resource.Error -> _uiState.send(UiState.Error(response.message))
                    is Resource.Loading -> _uiState.send(UiState.Loading)
                }
            }
        }
    }
}