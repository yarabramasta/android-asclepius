package com.dicoding.asclepius.data.network

import com.dicoding.asclepius.domain.models.Article
import kotlinx.serialization.Serializable

@Serializable
data class ArticleEntity(
	val author: String? = null,
	val content: String,
	val description: String,
	val publishedAt: String,
	val source: Source,
	val title: String,
	val url: String,
	val urlToImage: String? = null,
)

fun ArticleEntity.toDomain() = Article(
	description = description,
	title = title,
	url = url,
	urlToImage = urlToImage,
)

@Serializable
data class Source(
	val id: String? = null,
	val name: String,
)
