package com.sanryoo.news.feature.domain.repository

import com.sanryoo.news.feature.domain.modal.Article
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun upsert(article: Article): Long

    fun getAllArticles(): Flow<List<Article>>

    suspend fun delete(article: Article)

}