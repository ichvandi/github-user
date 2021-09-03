package com.vandoc.githubuser.ui.main

import com.vandoc.githubuser.util.CoroutineTestRule
import com.vandoc.githubuser.util.FakeData
import com.vandoc.githubuser.util.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val repository = FakeRepository()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(coroutineTestRule.testDispatcherProvider, repository)
    }

    @Test
    fun shouldGetUsersOnInit() = coroutineTestRule.runBlockingTest {
        val job = launch {
            assertEquals(null, viewModel.users.first())
            assertEquals(FakeData.fakeUser, viewModel.users.last())
        }

        job.cancel()
    }
}