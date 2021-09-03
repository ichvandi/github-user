package com.vandoc.githubuser.di

import com.vandoc.githubuser.data.GithubDataSource
import com.vandoc.githubuser.data.GithubRepository
import com.vandoc.githubuser.data.remote.RemoteRepository
import com.vandoc.githubuser.util.AppDispatcher
import com.vandoc.githubuser.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDispatcherProvider(): DispatcherProvider = AppDispatcher()

    @Provides
    @Singleton
    fun providesGithubRepository(remoteRepository: RemoteRepository): GithubDataSource =
        GithubRepository(remoteRepository)
}