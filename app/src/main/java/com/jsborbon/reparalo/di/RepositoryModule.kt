package com.jsborbon.reparalo.di

import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.data.repository.CommentRepository
import com.jsborbon.reparalo.data.repository.FavoritesRepository
import com.jsborbon.reparalo.data.repository.ForumRepository
import com.jsborbon.reparalo.data.repository.HelpRepository
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.data.repository.MaterialRepository
import com.jsborbon.reparalo.data.repository.NotificationRepository
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.data.repository.TermsRepository
import com.jsborbon.reparalo.data.repository.TutorialRepository
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.data.repository.impl.AuthRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.CommentRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.FavoritesRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.ForumRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.HelpRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.HistoryRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.MaterialRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.NotificationRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.TechnicianRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.TermsRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.TutorialRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * AppModule binds concrete repository implementations to their interfaces
 * using Dagger Hilt. Although it may appear unused in the IDE, Hilt processes
 * this module at compile time due to the @Module and @InstallIn annotations.
 *
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl,
    ): FavoritesRepository

    @Binds
    abstract fun bindForumRepository(
        impl: ForumRepositoryImpl,
    ): ForumRepository

    @Binds
    abstract fun bindHistoryRepository(
        impl: HistoryRepositoryImpl,
    ): HistoryRepository

    @Binds
    abstract fun bindMaterialRepository(
        impl: MaterialRepositoryImpl,
    ): MaterialRepository

    @Binds
    abstract fun bindTechnicianRepository(
        impl: TechnicianRepositoryImpl,
    ): TechnicianRepository

    @Binds
    abstract fun bindTutorialRepository(
        impl: TutorialRepositoryImpl,
    ): TutorialRepository

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    abstract fun bindTermsRepository(
        impl: TermsRepositoryImpl,
    ): TermsRepository

    @Binds
    abstract fun bindHelpRepository(
        impl: HelpRepositoryImpl,
    ): HelpRepository

    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl,
    ): NotificationRepository

    @Binds
    abstract fun bindCommentRepository(
        impl: CommentRepositoryImpl,
    ): CommentRepository
}
