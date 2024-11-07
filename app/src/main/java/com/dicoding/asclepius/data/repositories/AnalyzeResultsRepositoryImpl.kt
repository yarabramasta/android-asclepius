package com.dicoding.asclepius.data.repositories

import com.dicoding.asclepius.data.local.AnalyzeResultDao
import com.dicoding.asclepius.data.local.toDomain
import com.dicoding.asclepius.di.IoDispatcher
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.models.toEntity
import com.dicoding.asclepius.domain.repositories.AnalyzeResultsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyzeResultsRepositoryImpl @Inject constructor(
	private val dao: AnalyzeResultDao,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AnalyzeResultsRepository {

	override fun getAnalyzeResults(): Flow<Pair<List<AnalyzeResult>, Throwable?>> {
		return flow {
			try {
				val results = dao.getAllResults()
				emit(Pair(results.map { it.toDomain() }, null))
			} catch (e: Exception) {
				emit(Pair(emptyList(), e))
			}
		}.flowOn(ioDispatcher)
	}

	override suspend fun saveAnalyzeResult(result: AnalyzeResult): Pair<Boolean, Throwable?> {
		return withContext(ioDispatcher) {
			try {
				dao.saveResult(result.toEntity())
				Pair(true, null)
			} catch (e: Exception) {
				Pair(false, e)
			}
		}
	}

	override suspend fun deleteAnalyzeResult(result: AnalyzeResult): Pair<Boolean, Throwable?> {
		return withContext(ioDispatcher) {
			try {
				dao.deleteResult(result.imageUri.toString())
				Pair(true, null)
			} catch (e: Exception) {
				Pair(false, e)
			}
		}
	}
}