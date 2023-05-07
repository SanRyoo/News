package com.sanryoo.news.feature.domain.repository.impl

import com.sanryoo.news.feature.data.api.NewsAPI
import com.sanryoo.news.feature.domain.modal.NewsResponse
import com.sanryoo.news.feature.domain.repository.APIRepository
import retrofit2.Response
import javax.inject.Inject

class APIRepositoryImpl @Inject constructor(
    private val api: NewsAPI
) : APIRepository {

    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int,
        apikey: String
    ): Response<NewsResponse> {
        return api.getBreakingNews(countryCode, pageNumber, apikey)
    }

    override suspend fun searchForNews(
        searchQuery: String,
        pageNumber: Int,
        apikey: String
    ): Response<NewsResponse> {
        return api.searchForNews(searchQuery, pageNumber, apikey)
    }

}