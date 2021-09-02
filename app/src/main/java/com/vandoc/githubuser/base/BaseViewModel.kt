package com.vandoc.githubuser.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel : ViewModel() {
    protected val _uiState = Channel<UiState>()
    val uiState = _uiState.receiveAsFlow()

    sealed class UiState {
        data class Success(val message: String? = null) : UiState()
        data class Error(val message: String) : UiState()
        object Loading : UiState()
    }

}