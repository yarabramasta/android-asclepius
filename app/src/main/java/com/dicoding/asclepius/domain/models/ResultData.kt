package com.dicoding.asclepius.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultData(
	val imageUri: Uri,
	val confidenceScore: String,
) : Parcelable
