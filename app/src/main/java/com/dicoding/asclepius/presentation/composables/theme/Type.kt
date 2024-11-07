package com.dicoding.asclepius.presentation.composables.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
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

@Composable
fun getAppTypography(colorScheme: ColorScheme) = Typography(
	displayLarge = baseline.displayLarge.copy(
		fontFamily = CabinetGrotesk,
		color = colorScheme.onBackground,
	),
	displayMedium = baseline.displayMedium.copy(
		fontFamily = CabinetGrotesk,
		color = colorScheme.onBackground,
	),
	displaySmall = baseline.displaySmall.copy(
		fontFamily = CabinetGrotesk,
		color = colorScheme.onBackground,
	),
	headlineLarge = baseline.headlineLarge.copy(
		fontFamily = CabinetGrotesk,
		color = colorScheme.onBackground,
	),
	headlineMedium = baseline.headlineMedium.copy(
		fontFamily = CabinetGrotesk,
		color = colorScheme.onBackground,
	),
	headlineSmall = baseline.headlineSmall.copy(
		fontFamily = CabinetGrotesk,
		color = colorScheme.onBackground,
	),
	titleLarge = baseline.titleLarge.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	titleMedium = baseline.titleMedium.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	titleSmall = baseline.titleSmall.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	bodyLarge = baseline.bodyLarge.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	bodyMedium = baseline.bodyMedium.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	bodySmall = baseline.bodySmall.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	labelLarge = baseline.labelLarge.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	labelMedium = baseline.labelMedium.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
	labelSmall = baseline.labelSmall.copy(
		fontFamily = Satoshi,
		color = colorScheme.onBackground,
	),
)
