package com.sanryoo.news.feature.data.api

import com.sanryoo.news.feature.domain.modal.NewsResponse
import com.sanryoo.news.feature.util.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apikey: String = Constant.API_KEY
    ): Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apikey: String = Constant.API_KEY
    ): Response<NewsResponse>

}