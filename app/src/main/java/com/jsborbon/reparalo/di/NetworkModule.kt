package com.jsborbon.reparalo.di

import com.jsborbon.reparalo.data.api.AuthInterceptor
import com.jsborbon.reparalo.data.api.service.CommentApiService
import com.jsborbon.reparalo.data.api.service.HelpApiService
import com.jsborbon.reparalo.data.api.service.TutorialApiService
import com.jsborbon.reparalo.data.api.service.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * NetworkModule configures Retrofit, OkHttpClient, and API services
 * for dependency injection using Dagger Hilt.
 *
 * Although this object and its @Provides functions may appear unused,
 * Hilt automatically detects and uses them at compile time because of
 * the @Module and @InstallIn annotations.
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.reparalo.com/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideTutorialApiService(retrofit: Retrofit): TutorialApiService =
        retrofit.create(TutorialApiService::class.java)

    @Provides
    @Singleton
    fun provideCommentApiService(retrofit: Retrofit): CommentApiService =
        retrofit.create(CommentApiService::class.java)

    @Provides
    @Singleton
    fun provideHelpApiService(retrofit: Retrofit): HelpApiService =
        retrofit.create(HelpApiService::class.java)
}
