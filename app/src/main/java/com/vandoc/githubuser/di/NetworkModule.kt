package com.vandoc.githubuser.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vandoc.githubuser.BuildConfig
import com.vandoc.githubuser.data.remote.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverter(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { addHeaderInterceptor(it) }
            .build()

    private fun addHeaderInterceptor(it: Interceptor.Chain): Response {
        val request = it.request()
            .newBuilder()
            .addHeader("Authorization", "token ${BuildConfig.TOKEN}")
            .build()
        return it.proceed(request)
    }

    @Provides
    @Singleton
    fun provideRetrofit(converter: MoshiConverterFactory, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(converter)
            .baseUrl(BuildConfig.BASE_URL)
            .build()


    @Provides
    @Singleton
    fun provideGithubService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)

}