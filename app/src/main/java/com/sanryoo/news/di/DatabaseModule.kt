package com.sanryoo.news.di

import android.content.Context
import com.sanryoo.news.feature.domain.database.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = ArticleDatabase(context)

    @Provides
    @Singleton
    fun provideArticleDAO(articleDatabase: ArticleDatabase) = articleDatabase.getArticleDAO()

}