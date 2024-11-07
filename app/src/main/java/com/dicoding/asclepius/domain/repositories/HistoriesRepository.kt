package com.dicoding.asclepius.domain.repositories

import com.dicoding.asclepius.domain.models.AnalyzeResult
import kotlinx.coroutines.flow.Flow

interface HistoriesRepository {
	fun getHistories(): Flow<Pair<List<AnalyzeResult>, Throwable?>>

	suspend fun saveResult(result: AnalyzeResult): Pair<Boolean, Throwable?>

	suspend fun deleteResult(result: AnalyzeResult): Pair<Boolean, Throwable?>
}