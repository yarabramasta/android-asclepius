package com.dicoding.asclepius.data.local

import androidx.room.*

@Dao
interface HistoriesDao {
	@Query("SELECT * FROM histories")
	suspend fun getHistories(): List<AnalyzeResultEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveResult(analyzeResult: AnalyzeResultEntity)

	@Query("DELETE FROM histories WHERE id = :id")
	suspend fun deleteResult(id: Int)
}