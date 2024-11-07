package com.dicoding.asclepius.data.network

import kotlinx.serialization.Serializable

@Serializable
data class GetNewsResponse(
	val articles: List<ArticleEntity>,
	val status: String,
	val totalResults: Long,
)
