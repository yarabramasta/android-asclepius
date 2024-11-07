package com.dicoding.asclepius.data.repositories

import com.dicoding.asclepius.data.network.NewsApi
import com.dicoding.asclepius.data.network.toDomain
import com.dicoding.asclepius.di.IoDispatcher
import com.dicoding.asclepius.domain.models.Article
import com.dicoding.asclepius.domain.repositories.NewsRepository
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
	private val api: NewsApi,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : NewsRepository {

	override fun getNews(): Flow<Pair<List<Article>, Throwable?>> =
		flow<Pair<List<Article>, Throwable?>> {
			api.getNews()
				.suspendOnSuccess {
					if (data.status != "ok") emit(Pair(emptyList(), Throwable("Failed to get news...")))
					else {
						emit(
							Pair(
								data.articles
									.filter { !it.source.name.lowercase().contains("removed") }
									.map { it.toDomain() },
								null
							)
						)
					}
				}
				.suspendOnFailure {
					emit(Pair(emptyList(), Throwable(messageOrNull ?: "Failed to get news...")))
				}
		}.flowOn(ioDispatcher)
}