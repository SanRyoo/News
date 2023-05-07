package com.sanryoo.news.feature.domain.repository

import com.sanryoo.news.feature.domain.modal.NewsResponse
import com.sanryoo.news.feature.util.Constant
import retrofit2.Response
import retrofit2.http.Query

interface APIRepository {

    suspend fun getBreakingNews(
        countryCode: String = "us",
        pageNumber: Int = 1,
        apikey: String = Constant.API_KEY
    ): Response<NewsResponse>

    suspend fun searchForNews(
        searchQuery: String,
        pageNumber: Int = 1,
        apikey: String = Constant.API_KEY
    ): Response<NewsResponse>

}