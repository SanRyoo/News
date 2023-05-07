package com.sanryoo.news.feature.domain.repository.impl

import com.sanryoo.news.feature.domain.database.ArticleDAO
import com.sanryoo.news.feature.domain.modal.Article
import com.sanryoo.news.feature.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val articleDAO: ArticleDAO
) : DatabaseRepository {

    override suspend fun upsert(article: Article): Long {
        return articleDAO.upsert(article)
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return articleDAO.getAllArticles()
    }

    override suspend fun delete(article: Article) {
        articleDAO.deleteArticle(article)
    }

}