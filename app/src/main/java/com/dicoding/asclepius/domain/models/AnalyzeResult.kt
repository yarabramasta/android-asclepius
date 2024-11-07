package com.dicoding.asclepius.domain.models

import android.net.Uri
import android.os.Parcelable
import com.dicoding.asclepius.data.local.AnalyzeResultEntity
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class AnalyzeResult(
	val imageUri: Uri,
	val label: String,
	val confidenceScore: Float,
) : Parcelable

fun AnalyzeResult.getConfidenceScoreString(): String {
	val percentage = (confidenceScore * 100).roundToInt()
	return "$label ($percentage%)"
}

fun AnalyzeResult.toEntity(): AnalyzeResultEntity {
	return AnalyzeResultEntity(
		imageUri = imageUri.toString(),
		label = label,
		confidenceScore = confidenceScore,
	)
}