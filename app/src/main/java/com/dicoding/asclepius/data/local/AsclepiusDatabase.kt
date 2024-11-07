package com.dicoding.asclepius.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
	entities = [AnalyzeResultEntity::class], version = 1,
	exportSchema = false
)
abstract class AsclepiusDatabase : RoomDatabase() {
	abstract fun historiesDao(): HistoriesDao
}