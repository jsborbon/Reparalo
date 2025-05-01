package com.jsborbon.reparalo.di

import com.jsborbon.reparalo.data.AuthRepository
import com.jsborbon.reparalo.data.DataRepository
import com.jsborbon.reparalo.data.ReparaloAuthRepository
import com.jsborbon.reparalo.data.ReparaloRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: ReparaloAuthRepository,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDataRepository(
        impl: ReparaloRepositoryImpl,
    ): DataRepository
}
