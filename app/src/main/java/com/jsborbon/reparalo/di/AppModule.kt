package com.jsborbon.reparalo.di

import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.data.repository.impl.AuthRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.TechnicianRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule binds concrete repository implementations to their interfaces
 * using Dagger Hilt. Although it may appear unused in the IDE, Hilt processes
 * this module at compile time due to the @Module and @InstallIn annotations.
 *
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTechnicianRepository(
        impl: TechnicianRepositoryImpl
    ): TechnicianRepository
}
