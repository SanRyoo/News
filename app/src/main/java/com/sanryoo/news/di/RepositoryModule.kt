package com.sanryoo.news.di

import com.sanryoo.news.feature.domain.repository.APIRepository
import com.sanryoo.news.feature.domain.repository.DatabaseRepository
import com.sanryoo.news.feature.domain.repository.impl.APIRepositoryImpl
import com.sanryoo.news.feature.domain.repository.impl.DatabaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun provideAPIRepository(impl: APIRepositoryImpl) : APIRepository

    @Binds
    @ViewModelScoped
    abstract fun provideDatabaseRepository(impl: DatabaseRepositoryImpl) : DatabaseRepository

}