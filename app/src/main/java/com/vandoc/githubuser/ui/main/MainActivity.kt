package com.vandoc.githubuser.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vandoc.githubuser.base.BaseViewModel
import com.vandoc.githubuser.databinding.ActivityMainBinding
import com.vandoc.githubuser.model.User
import com.vandoc.githubuser.util.disable
import com.vandoc.githubuser.util.enable
import com.vandoc.githubuser.util.gone
import com.vandoc.githubuser.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var userAdapter: UserAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        userAdapter = UserAdapter(this::handleItemClick)
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(binding.rvUser.context, manager.orientation)

        binding.rvUser.apply {
            adapter = userAdapter
            layoutManager = manager
            addItemDecoration(decoration)
            addOnScrollListener(scrollListener)
        }

        binding.refresh.setOnRefreshListener {
            mainViewModel.getUsers(0)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.users.collect {
                        it?.let {
                            if (it.isEmpty()) {
                                showPlaceholder()
                            } else {
                                showData()
                            }
                        }

                        userAdapter.submitList(it)
                    }
                }

                launch {
                    mainViewModel.uiState.collect { handleUiState(it) }
                }
            }
        }
    }

    private fun handleItemClick(user: User) {
        Toast.makeText(this, "$user", Toast.LENGTH_SHORT).show()
    }

    private fun handleUiState(state: BaseViewModel.UiState) {
        when (state) {
            is BaseViewModel.UiState.Success -> {
                hideSwipeRefresh()
                enableSwipeRefresh()
            }
            is BaseViewModel.UiState.Error -> {
                if (mainViewModel.users.value == null) {
                    showPlaceholder()
                }

                hideSwipeRefresh()
                enableSwipeRefresh()

                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is BaseViewModel.UiState.Loading -> {
                disableSwipeRefresh()
            }
        }
    }

    private fun enableSwipeRefresh() {
        binding.refresh.enable()
        binding.progressBar.gone()
    }

    private fun disableSwipeRefresh() {
        binding.refresh.disable()
        binding.progressBar.visible()
    }

    private fun hideSwipeRefresh() {
        if (binding.refresh.isRefreshing) {
            binding.refresh.isRefreshing = false
        }
    }

    private fun showData() {
        binding.ivPlaceholder.gone()
        binding.rvUser.visible()
    }

    private fun showPlaceholder() {
        binding.ivPlaceholder.visible()
        binding.rvUser.gone()
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val manager = recyclerView.layoutManager as LinearLayoutManager
            val countItem = manager.itemCount
            val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
            val isLastPosition = countItem - 1 == lastVisiblePosition

            if (isLastPosition && !binding.refresh.isRefreshing) {
                mainViewModel.getUsers()
            }
        }
    }
}