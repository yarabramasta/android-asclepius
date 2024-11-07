package com.dicoding.asclepius.domain.repositories

import com.dicoding.asclepius.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
	fun getNews(): Flow<Pair<List<Article>, Throwable?>>
}