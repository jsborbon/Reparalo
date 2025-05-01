package com.jsborbon.reparalo.di

import com.jsborbon.reparalo.data.api.ReparaloApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://tuservidor.com/api/" // Ajusta según tu API

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideReparaloApiService(retrofit: Retrofit): ReparaloApiService =
        retrofit.create(ReparaloApiService::class.java)
}
