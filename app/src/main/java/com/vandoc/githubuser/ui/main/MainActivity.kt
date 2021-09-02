package com.vandoc.githubuser.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vandoc.githubuser.databinding.ActivityMainBinding
import com.vandoc.githubuser.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        userAdapter = UserAdapter(this::handleItemClick)
        val manager = LinearLayoutManager(this)

        binding.rvUser.apply {
            adapter = userAdapter
            layoutManager = manager
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.users.collect {
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
}