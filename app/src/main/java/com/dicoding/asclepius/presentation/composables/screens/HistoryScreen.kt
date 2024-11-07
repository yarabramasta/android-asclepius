package com.dicoding.asclepius.presentation.composables.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.models.getConfidenceScoreString
import com.dicoding.asclepius.presentation.composables.theme.AppTheme
import com.dicoding.asclepius.presentation.composables.utils.rememberFlowWithLifecycle
import com.dicoding.asclepius.presentation.viewmodel.AnalyzeResultsViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
	modifier: Modifier = Modifier,
	vm: AnalyzeResultsViewModel,
) {
	val state by vm.state.collectAsStateWithLifecycle()
	val effect = rememberFlowWithLifecycle(vm.effect)
	val context = LocalContext.current

	LaunchedEffect(effect) {
		effect.collect {
			when (it) {
				is AnalyzeResultsViewModel.Effect.ShowError -> {
					Toast.makeText(
						context,
						it.error.message ?: "Something went wrong...",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}

	PullToRefreshBox(
		isRefreshing = state.isRefreshing,
		onRefresh = { vm.add(AnalyzeResultsViewModel.Event.OnRefresh) },
		modifier = Modifier.fillMaxSize()
	) {
		HistoryScreenContent(
			modifier = modifier,
			results = state.results
		)
	}
}

@Composable
private fun HistoryScreenContent(
	modifier: Modifier = Modifier,
	results: List<AnalyzeResult>,
) {
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(24.dp)
	) {
		item {
			Text(
				text = "History",
				style = MaterialTheme.typography.headlineSmall,
			)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = "This is a list of all the images you've analyzed.",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			Spacer(modifier = Modifier.height(24.dp))
		}

		items(
			items = results,
			key = { it.id ?: Random.nextInt() }
		) { result ->
			Text(text = result.getConfidenceScoreString())
		}
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryScreenPreview() {
	val items = listOf(
		AnalyzeResult.fake(),
		AnalyzeResult.fake(),
		AnalyzeResult.fake(),
		AnalyzeResult.fake(),
		AnalyzeResult.fake(),
	).distinctBy { it.id ?: Random.nextInt() }

	AppTheme {
		HistoryScreenContent(results = items)
	}
}