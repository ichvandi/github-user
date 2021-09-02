package com.vandoc.githubuser.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vandoc.githubuser.databinding.ActivityMainBinding
import com.vandoc.githubuser.model.User
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
                        if (binding.refresh.isRefreshing) {
                            binding.refresh.isRefreshing = false
                        }
                        userAdapter.submitList(it)
                    }
                }

                launch {
                    mainViewModel.uiState.collect {
                        Log.e("MainState", it.toString())
                    }
                }
            }
        }
    }

    private fun handleItemClick(user: User) {
        Toast.makeText(this, "$user", Toast.LENGTH_SHORT).show()
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