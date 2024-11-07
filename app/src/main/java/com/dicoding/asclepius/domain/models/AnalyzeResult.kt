package com.dicoding.asclepius.domain.models

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.AnalyzeResultEntity
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt
import kotlin.random.Random

@Immutable
@Parcelize
data class AnalyzeResult(
	val id: Int = 0,
	val imageUri: Uri,
	val label: String,
	val confidenceScore: Float,
) : Parcelable {
	companion object {
		fun fake() = AnalyzeResult(
			id = Random.nextInt(),
			imageUri = Uri.parse("android.resource://com.dicoding.asclepius/${R.drawable.ic_launcher_background}"),
			label = "Non Cancer",
			confidenceScore = 0.5f,
		)
	}
}

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