package com.sanryoo.news.feature.domain.modal

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String = "",
    val name: String = ""
)