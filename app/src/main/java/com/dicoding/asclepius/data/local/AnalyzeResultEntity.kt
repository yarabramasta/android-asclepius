package com.dicoding.asclepius.data.local

import android.net.Uri
import androidx.room.*
import com.dicoding.asclepius.domain.models.AnalyzeResult

@Entity(tableName = "analyze_result")
data class AnalyzeResultEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "image_uri") val imageUri: String,
	@ColumnInfo val label: String,
	@ColumnInfo val confidenceScore: Float,
)

fun AnalyzeResultEntity.toDomain() = AnalyzeResult(
	id = id,
	imageUri = Uri.parse(imageUri),
	label = label,
	confidenceScore = confidenceScore
)
