package com.dicoding.asclepius.data.repositories

import com.dicoding.asclepius.data.local.HistoriesDao
import com.dicoding.asclepius.data.local.toDomain
import com.dicoding.asclepius.di.IoDispatcher
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.models.toEntity
import com.dicoding.asclepius.domain.repositories.HistoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoriesRepositoryImpl @Inject constructor(
	private val dao: HistoriesDao,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : HistoriesRepository {

	override fun getHistories(): Flow<Pair<List<AnalyzeResult>, Throwable?>> {
		return flow {
			try {
				val results = dao.getHistories()
				emit(Pair(results.map { it.toDomain() }, null))
			} catch (e: Exception) {
				emit(Pair(emptyList(), e))
			}
		}.flowOn(ioDispatcher)
	}

	override suspend fun saveResult(result: AnalyzeResult): Pair<Boolean, Throwable?> {
		return withContext(ioDispatcher) {
			try {
				dao.saveResult(result.toEntity())
				Pair(true, null)
			} catch (e: Exception) {
				Pair(false, e)
			}
		}
	}

	override suspend fun deleteResult(result: AnalyzeResult): Pair<Boolean, Throwable?> {
		return withContext(ioDispatcher) {
			try {
				dao.deleteResult(result.id)
				Pair(true, null)
			} catch (e: Exception) {
				Pair(false, e)
			}
		}
	}
}