package com.dicoding.asclepius.domain.repositories

import com.dicoding.asclepius.domain.models.AnalyzeResult
import kotlinx.coroutines.flow.Flow

interface AnalyzeResultsRepository {

	fun getAnalyzeResults(): Flow<Pair<List<AnalyzeResult>, Throwable?>>

	suspend fun saveAnalyzeResult(result: AnalyzeResult): Pair<Boolean, Throwable?>

	suspend fun deleteAnalyzeResult(result: AnalyzeResult): Pair<Boolean, Throwable?>
}