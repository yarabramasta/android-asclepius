package com.dicoding.asclepius.data.local

import androidx.room.*

@Dao
interface AnalyzeResultDao {
	@Query("SELECT * FROM analyze_result")
	suspend fun getAllResults(): List<AnalyzeResultEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveResult(analyzeResult: AnalyzeResultEntity)

	@Query("DELETE FROM analyze_result WHERE id = :id")
	suspend fun deleteResult(id: Int)
}