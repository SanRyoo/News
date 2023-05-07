package com.sanryoo.news.feature.domain.modal

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)