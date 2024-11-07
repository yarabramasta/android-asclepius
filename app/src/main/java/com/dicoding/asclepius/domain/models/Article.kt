package com.dicoding.asclepius.domain.models

import android.net.Uri
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Article(
	val description: String,
	val title: String,
	val url: String,
	val urlToImage: String? = null,
)

fun Article.getBrowsableUri(): Uri = Uri.parse(url)