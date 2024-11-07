package com.dicoding.asclepius.presentation.composables.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.*
import com.dicoding.asclepius.R

val Satoshi = FontFamily(
	Font(R.font.satoshi_regular, FontWeight.Normal, FontStyle.Normal),
	Font(R.font.satoshi_medium, FontWeight.Medium, FontStyle.Normal),
)
val CabinetGrotesk = FontFamily(
	Font(R.font.cabinet_grotesk_bold, FontWeight.Bold, FontStyle.Normal)
)

val baseline = Typography()

val AppTypography = Typography(
	displayLarge = baseline.displayLarge.copy(fontFamily = CabinetGrotesk),
	displayMedium = baseline.displayMedium.copy(fontFamily = CabinetGrotesk),
	displaySmall = baseline.displaySmall.copy(fontFamily = CabinetGrotesk),
	headlineLarge = baseline.headlineLarge.copy(fontFamily = CabinetGrotesk),
	headlineMedium = baseline.headlineMedium.copy(fontFamily = CabinetGrotesk),
	headlineSmall = baseline.headlineSmall.copy(fontFamily = CabinetGrotesk),
	titleLarge = baseline.titleLarge.copy(fontFamily = Satoshi),
	titleMedium = baseline.titleMedium.copy(fontFamily = Satoshi),
	titleSmall = baseline.titleSmall.copy(fontFamily = Satoshi),
	bodyLarge = baseline.bodyLarge.copy(fontFamily = Satoshi),
	bodyMedium = baseline.bodyMedium.copy(fontFamily = Satoshi),
	bodySmall = baseline.bodySmall.copy(fontFamily = Satoshi),
	labelLarge = baseline.labelLarge.copy(fontFamily = Satoshi),
	labelMedium = baseline.labelMedium.copy(fontFamily = Satoshi),
	labelSmall = baseline.labelSmall.copy(fontFamily = Satoshi),
)
